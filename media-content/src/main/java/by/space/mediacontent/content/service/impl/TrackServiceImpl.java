package by.space.mediacontent.content.service.impl;

import by.space.mediacontent.artist.domain.entity.ArtistPlaylistEntity;
import by.space.mediacontent.artist.domain.entity.ArtistTrackEntity;
import by.space.mediacontent.artist.infrastructure.repository.ArtistPlaylistRepository;
import by.space.mediacontent.artist.infrastructure.repository.ArtistRepository;
import by.space.mediacontent.artist.infrastructure.repository.ArtistTrackRepository;
import by.space.mediacontent.content.domain.entity.GenreEntity;
import by.space.mediacontent.content.domain.entity.TrackEntity;
import by.space.mediacontent.content.domain.entity.TrackGenreEntity;
import by.space.mediacontent.content.domain.entity.TrackPlaylistEntity;
import by.space.mediacontent.content.domain.enums.GenreSource;
import by.space.mediacontent.content.dto.TrackGenreAssignmentDto;
import by.space.mediacontent.content.dto.TrackResponseDto;
import by.space.mediacontent.content.repository.GenreRepository;
import by.space.mediacontent.content.repository.PlaylistRepository;
import by.space.mediacontent.content.repository.TrackGenreRepository;
import by.space.mediacontent.content.repository.TrackPlaylistRepository;
import by.space.mediacontent.content.repository.TrackRepository;
import by.space.mediacontent.content.service.MinioStorageService;
import by.space.mediacontent.content.service.TrackService;
import by.space.mediacontent.content.util.MediaTypeUtil;
import by.space.mediacontent.content.util.ObjectKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final ArtistTrackRepository artistTrackRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final TrackGenreRepository trackGenreRepository;
    private final ArtistPlaylistRepository artistPlaylistRepository;
    private final TrackPlaylistRepository trackPlaylistRepository;
    private final PlaylistRepository playlistRepository;
    private final MinioStorageService minioStorageService;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    @Transactional
    public TrackResponseDto createTrackForArtist(
        final Long artistId,
        final Long ownerId,
        final String name,
        final Long idCover,
        final Long durationSeconds,
        final List<Long> genreIds,
        final MultipartFile file
    ) {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new IllegalArgumentException("file required");
        }
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("name required");
        }
        if (Objects.isNull(ownerId)) {
            throw new IllegalArgumentException("ownerId required");
        }
        final var artist = artistRepository.findById(artistId)
            .orElseThrow(() -> new IllegalArgumentException("artist not found"));
        if (artist.isDeleted()) {
            throw new IllegalArgumentException("artist deleted");
        }

        final String originalName = Objects.toString(file.getOriginalFilename(), "track");
        final String objectKey = ObjectKeyGenerator.generate("tracks", ownerId, originalName);
        minioStorageService.upload(file, objectKey);

        long dur = 0L;
        if (Objects.nonNull(durationSeconds) && durationSeconds > 0) {
            dur = Math.min(durationSeconds, 86400L * 4);
        }

        final LocalDateTime now = LocalDateTime.now();
        final TrackEntity track = trackRepository.save(
            TrackEntity.builder()
                .name(name.trim())
                .bucket(bucket)
                .objectKey(objectKey)
                .originalFileName(originalName)
                .idCover(idCover)
                .duration(dur)
                .single(true)
                .removed(false)
                .createdAt(now)
                .updatedAt(now)
                .build()
        );

        artistTrackRepository.save(
            ArtistTrackEntity.builder()
                .artistId(artistId)
                .trackId(track.getId())
                .deleted(false)
                .createdAt(now)
                .build()
        );

        replaceTrackGenres(track.getId(), genreIds, GenreSource.MANUAL, null, now);

        return toDto(track, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackResponseDto> listTracksByArtist(final Long artistId) {
        final List<TrackResponseDto> out = new ArrayList<>();
        for (ArtistTrackEntity link : artistTrackRepository.findByArtistIdOrderByIdAsc(artistId)) {
            trackRepository.findById(link.getTrackId())
                .filter(t -> !t.isRemoved())
                .ifPresent(t -> out.add(toDto(t, link.isDeleted())));
        }
        return out;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackResponseDto> listAllTracks() {
        return trackRepository.findAllByRemovedIsFalseOrderByIdAsc().stream()
            .map(t -> toDto(t, false))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TrackResponseDto updateTrackGenres(
        final Long artistId,
        final Long trackId,
        final List<Long> genreIds,
        final GenreSource source,
        final BigDecimal confidence
    ) {
        artistTrackRepository.findFirstByArtistIdAndTrackIdAndDeletedFalse(artistId, trackId)
            .orElseThrow(() -> new IllegalArgumentException("track not linked to artist"));
        final TrackEntity track = trackRepository.findById(trackId)
            .filter(t -> !t.isRemoved())
            .orElseThrow(() -> new IllegalArgumentException("track not found"));
        final LocalDateTime now = LocalDateTime.now();
        replaceTrackGenres(trackId, genreIds, source, confidence, now);
        track.setUpdatedAt(now);
        trackRepository.save(track);
        final boolean deleted = artistTrackRepository.findFirstByArtistIdAndTrackId(artistId, trackId)
            .map(ArtistTrackEntity::isDeleted)
            .orElse(false);
        return toDto(track, deleted);
    }

    @Override
    @Transactional
    public void removeTrackFromArtist(final Long artistId, final Long trackId) {
        final LocalDateTime now = LocalDateTime.now();
        final Optional<ArtistTrackEntity> active =
            artistTrackRepository.findFirstByArtistIdAndTrackIdAndDeletedFalse(artistId, trackId);
        if (active.isPresent()) {
            final ArtistTrackEntity link = active.get();
            link.setDeleted(true);
            link.setDeletedAt(now);
            artistTrackRepository.save(link);
            return;
        }
        if (artistTrackRepository.findFirstByArtistIdAndTrackId(artistId, trackId).isPresent()) {
            return;
        }
        if (!isTrackOnArtistNonDeletedAlbum(artistId, trackId)) {
            throw new IllegalArgumentException("link not found");
        }
        trackRepository.findById(trackId)
            .filter(t -> !t.isRemoved())
            .orElseThrow(() -> new IllegalArgumentException("track not found"));
        artistTrackRepository.save(
            ArtistTrackEntity.builder()
                .artistId(artistId)
                .trackId(trackId)
                .deleted(true)
                .deletedAt(now)
                .createdAt(now)
                .build()
        );
    }

    private boolean isTrackOnArtistNonDeletedAlbum(final Long artistId, final Long trackId) {
        for (ArtistPlaylistEntity apl : artistPlaylistRepository.findByArtistIdOrderByIdAsc(artistId)) {
            if (apl.isDeleted()) {
                continue;
            }
            final Optional<TrackPlaylistEntity> tpl =
                trackPlaylistRepository.findFirstByIdPlaylistAndIdTrack(apl.getPlaylistId(), trackId);
            if (tpl.isPresent() && !tpl.get().isRemoved()) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void restoreTrackForArtist(final Long artistId, final Long trackId) {
        final ArtistTrackEntity link = artistTrackRepository
            .findFirstByArtistIdAndTrackId(artistId, trackId)
            .orElseThrow(() -> new IllegalArgumentException("link not found"));
        if (!link.isDeleted()) {
            return;
        }
        final TrackEntity track = trackRepository.findById(trackId)
            .orElseThrow(() -> new IllegalArgumentException("track not found"));
        if (track.isRemoved()) {
            throw new IllegalArgumentException("track already finalized removed");
        }
        link.setDeleted(false);
        link.setDeletedAt(null);
        artistTrackRepository.save(link);
        restoreDeletedAlbumContainersForTrack(artistId, trackId);
    }
    
    private void restoreDeletedAlbumContainersForTrack(final Long artistId, final Long trackId) {
        for (ArtistPlaylistEntity apl : artistPlaylistRepository.findByArtistIdOrderByIdAsc(artistId)) {
            if (!apl.isDeleted()) {
                continue;
            }
            final Optional<TrackPlaylistEntity> tpl =
                trackPlaylistRepository.findFirstByIdPlaylistAndIdTrack(apl.getPlaylistId(), trackId);
            if (tpl.isEmpty() || tpl.get().isRemoved()) {
                continue;
            }
            playlistRepository.findById(apl.getPlaylistId())
                .filter(p -> !p.isRemoved())
                .ifPresent(p -> {
                    apl.setDeleted(false);
                    apl.setDeletedAt(null);
                    artistPlaylistRepository.save(apl);
                });
        }
    }

    @Override
    @Transactional
    public void finalizeRemoveTrackFromArtist(final Long artistId, final Long trackId) {
        final ArtistTrackEntity link = artistTrackRepository
            .findFirstByArtistIdAndTrackId(artistId, trackId)
            .orElseThrow(() -> new IllegalArgumentException("link not found"));
        final LocalDateTime now = LocalDateTime.now();
        if (!link.isDeleted()) {
            link.setDeleted(true);
            link.setDeletedAt(now);
            artistTrackRepository.save(link);
        }
        final TrackEntity track = trackRepository.findById(trackId)
            .orElseThrow(() -> new IllegalArgumentException("track not found"));
        track.setRemoved(true);
        track.setUpdatedAt(now);
        trackRepository.save(track);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource streamTrackAudio(final Long trackId) {
        final TrackEntity track = trackRepository.findById(trackId)
            .orElseThrow(() -> new IllegalArgumentException("track not found"));
        if (track.isRemoved()) {
            throw new IllegalArgumentException("track removed");
        }
        return new InputStreamResource(minioStorageService.download(track.getObjectKey()));
    }

    @Override
    @Transactional(readOnly = true)
    public MediaType resolveTrackAudioMediaType(final Long trackId) {
        final TrackEntity track = trackRepository.findById(trackId)
            .orElseThrow(() -> new IllegalArgumentException("track not found"));
        return MediaTypeUtil.guessFromFileName(track.getOriginalFileName());
    }

    private void replaceTrackGenres(
        final Long trackId,
        final List<Long> rawIds,
        final GenreSource source,
        final BigDecimal confidence,
        final LocalDateTime now
    ) {
        trackGenreRepository.deleteByIdTrack(trackId);
        if (rawIds == null || rawIds.isEmpty()) {
            return;
        }
        final LinkedHashSet<Long> uniq = new LinkedHashSet<>();
        for (Long id : rawIds) {
            if (id != null) {
                uniq.add(id);
            }
        }
        final GenreSource safeSource = source != null ? source : GenreSource.MANUAL;
        for (Long gid : uniq) {
            final GenreEntity g = genreRepository.findById(gid)
                .filter(genre -> !genre.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("genre not found: " + gid));
            trackGenreRepository.save(
                TrackGenreEntity.builder()
                    .idTrack(trackId)
                    .idGenre(g.getId())
                    .addedAt(now)
                    .source(safeSource)
                    .confidence(confidence)
                    .build()
            );
        }
    }

    private List<TrackGenreAssignmentDto> listGenreAssignmentsForTrack(final Long trackId) {
        return trackGenreRepository.findByIdTrackOrderByIdAsc(trackId).stream()
            .filter(link -> link.getIdGenre() != null)
            .map(link -> TrackGenreAssignmentDto.builder()
                .genreId(link.getIdGenre())
                .source(link.getSource())
                .confidence(link.getConfidence())
                .build())
            .collect(Collectors.toList());
    }

    private List<Long> listGenreIdsForTrack(final Long trackId) {
        return listGenreAssignmentsForTrack(trackId).stream()
            .map(TrackGenreAssignmentDto::getGenreId)
            .collect(Collectors.toList());
    }

    private List<String> listUniqueArtistNamesForTrack(final Long trackId) {
        final LinkedHashSet<String> seen = new LinkedHashSet<>();
        final List<String> out = new ArrayList<>();
        for (ArtistTrackEntity link : artistTrackRepository.findByTrackIdAndDeletedFalseOrderByIdAsc(
            trackId
        )) {
            artistRepository.findById(link.getArtistId())
                .filter(a -> !a.isDeleted())
                .map(a -> Objects.toString(a.getName(), "").trim())
                .filter(s -> !s.isEmpty())
                .ifPresent(name -> {
                    final String key = name.toLowerCase(Locale.ROOT);
                    if (seen.add(key)) {
                        out.add(name);
                    }
                });
        }
        return out;
    }

    private TrackResponseDto toDto(final TrackEntity t, final boolean deleted) {
        final Long dur = t.getDuration() != null ? t.getDuration() : 0L;
        return TrackResponseDto.builder()
            .id(t.getId())
            .name(t.getName())
            .idCover(t.getIdCover())
            .originalFileName(t.getOriginalFileName())
            .durationSeconds(dur)
            .single(t.isSingle())
            .deleted(deleted)
            .genreIds(listGenreIdsForTrack(t.getId()))
            .genres(listGenreAssignmentsForTrack(t.getId()))
            .artistNames(listUniqueArtistNamesForTrack(t.getId()))
            .build();
    }
}
