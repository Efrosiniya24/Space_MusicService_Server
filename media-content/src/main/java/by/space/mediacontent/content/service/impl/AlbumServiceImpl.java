package by.space.mediacontent.content.service.impl;

import by.space.mediacontent.artist.domain.entity.ArtistPlaylistEntity;
import by.space.mediacontent.artist.domain.entity.ArtistTrackEntity;
import by.space.mediacontent.artist.infrastructure.repository.ArtistPlaylistRepository;
import by.space.mediacontent.artist.infrastructure.repository.ArtistRepository;
import by.space.mediacontent.artist.infrastructure.repository.ArtistTrackRepository;
import by.space.mediacontent.content.domain.entity.PlaylistEntity;
import by.space.mediacontent.content.domain.entity.TrackEntity;
import by.space.mediacontent.content.domain.entity.TrackGenreEntity;
import by.space.mediacontent.content.domain.entity.TrackPlaylistEntity;
import by.space.mediacontent.content.dto.AlbumCreateDto;
import by.space.mediacontent.content.dto.AlbumPatchDto;
import by.space.mediacontent.content.dto.AlbumResponseDto;
import by.space.mediacontent.content.dto.AlbumTrackDto;
import by.space.mediacontent.content.repository.PlaylistRepository;
import by.space.mediacontent.content.repository.TrackGenreRepository;
import by.space.mediacontent.content.repository.TrackPlaylistRepository;
import by.space.mediacontent.content.repository.TrackRepository;
import by.space.mediacontent.content.service.AlbumService;
import by.space.mediacontent.content.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private static final Pattern ALBUM_NAME_YEAR_SUFFIX =
        Pattern.compile("^(.*)\\s+\\((\\d{4})\\)\\s*$");

    private final PlaylistRepository playlistRepository;
    private final ArtistPlaylistRepository artistPlaylistRepository;
    private final TrackPlaylistRepository trackPlaylistRepository;
    private final TrackGenreRepository trackGenreRepository;
    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final ArtistTrackRepository artistTrackRepository;
    private final TrackService trackService;

    @Override
    @Transactional
    public AlbumResponseDto createAlbumForArtist(final Long artistId, final AlbumCreateDto request) {
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("request required");
        }
        final String name = Objects.toString(request.getName(), "").trim();
        if (name.isBlank()) {
            throw new IllegalArgumentException("album name required");
        }
        final var artist = artistRepository.findById(artistId)
            .orElseThrow(() -> new IllegalArgumentException("artist not found"));
        if (artist.isDeleted()) {
            throw new IllegalArgumentException("artist deleted");
        }

        final LocalDateTime now = LocalDateTime.now();
        final PlaylistEntity album = playlistRepository.save(
            PlaylistEntity.builder()
                .name(name)
                .idCover(request.getIdCover())
                .idCreator(artistId)
                .removed(false)
                .createdAt(now)
                .updatedAt(now)
                .build()
        );

        artistPlaylistRepository.save(
            ArtistPlaylistEntity.builder()
                .artistId(artistId)
                .playlistId(album.getId())
                .deleted(false)
                .createdAt(now)
                .build()
        );

        final Set<Long> trackIds = new LinkedHashSet<>();
        if (request.getTrackIds() != null) {
            trackIds.addAll(request.getTrackIds());
        }
        for (Long trackId : trackIds) {
            if (trackId == null) continue;
            artistTrackRepository
                .findFirstByArtistIdAndTrackIdAndDeletedFalse(artistId, trackId)
                .orElseThrow(() -> new IllegalArgumentException("track not linked to artist: " + trackId));
            final TrackEntity track = trackRepository.findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("track not found: " + trackId));
            if (track.isRemoved()) {
                throw new IllegalArgumentException("track removed: " + trackId);
            }
            track.setSingle(false);
            track.setUpdatedAt(now);
            trackRepository.save(track);
            trackPlaylistRepository.save(
                TrackPlaylistEntity.builder()
                    .idPlaylist(album.getId())
                    .idTrack(trackId)
                    .removed(false)
                    .addedAt(now)
                    .build()
            );
        }

        return mapAlbum(album, artistId, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumResponseDto> listAlbumsByArtist(final Long artistId) {
        final List<AlbumResponseDto> out = new ArrayList<>();
        for (ArtistPlaylistEntity link : artistPlaylistRepository.findByArtistIdOrderByIdAsc(artistId)) {
            playlistRepository.findById(link.getPlaylistId())
                .filter(p -> !p.isRemoved())
                .ifPresent(p -> out.add(mapAlbum(p, artistId, link.isDeleted())));
        }
        return out;
    }

    @Override
    @Transactional
    public void removeAlbumFromArtist(final Long artistId, final Long albumId) {
        final ArtistPlaylistEntity link = artistPlaylistRepository
            .findFirstByArtistIdAndPlaylistIdAndDeletedFalse(artistId, albumId)
            .orElseThrow(() -> new IllegalArgumentException("album link not found"));
        final LocalDateTime now = LocalDateTime.now();
        link.setDeleted(true);
        link.setDeletedAt(now);
        artistPlaylistRepository.save(link);
        softDeleteArtistTracksForAlbum(artistId, albumId, now);
    }

    @Override
    @Transactional
    public void restoreAlbumForArtist(final Long artistId, final Long albumId) {
        final ArtistPlaylistEntity link = artistPlaylistRepository
            .findFirstByArtistIdAndPlaylistId(artistId, albumId)
            .orElseThrow(() -> new IllegalArgumentException("album link not found"));
        if (!link.isDeleted()) return;
        final PlaylistEntity album = playlistRepository.findById(albumId)
            .orElseThrow(() -> new IllegalArgumentException("album not found"));
        if (album.isRemoved()) {
            throw new IllegalArgumentException("album already finalized removed");
        }
        link.setDeleted(false);
        link.setDeletedAt(null);
        artistPlaylistRepository.save(link);
        restoreArtistTracksForAlbum(artistId, albumId);
    }

    @Override
    @Transactional
    public void finalizeRemoveAlbumFromArtist(final Long artistId, final Long albumId) {
        final ArtistPlaylistEntity link = artistPlaylistRepository
            .findFirstByArtistIdAndPlaylistId(artistId, albumId)
            .orElseThrow(() -> new IllegalArgumentException("album link not found"));
        final LocalDateTime now = LocalDateTime.now();
        if (!link.isDeleted()) {
            link.setDeleted(true);
            link.setDeletedAt(now);
            artistPlaylistRepository.save(link);
        }
        final PlaylistEntity album = playlistRepository.findById(albumId)
            .orElseThrow(() -> new IllegalArgumentException("album not found"));
        album.setRemoved(true);
        album.setUpdatedAt(now);
        playlistRepository.save(album);
        finalizeArtistTracksForAlbum(artistId, albumId);
    }

    @Override
    @Transactional
    public AlbumResponseDto addTrackToAlbum(final Long artistId, final Long albumId, final Long trackId) {
        if (artistId == null || albumId == null || trackId == null) {
            throw new IllegalArgumentException("artistId, albumId and trackId required");
        }
        artistPlaylistRepository
            .findFirstByArtistIdAndPlaylistIdAndDeletedFalse(artistId, albumId)
            .orElseThrow(() -> new IllegalArgumentException("album not linked to artist"));
        artistTrackRepository
            .findFirstByArtistIdAndTrackIdAndDeletedFalse(artistId, trackId)
            .orElseThrow(() -> new IllegalArgumentException("track not linked to artist"));
        final LocalDateTime now = LocalDateTime.now();
        trackRepository.findById(trackId)
            .filter(t -> !t.isRemoved())
            .map(t -> {
                t.setSingle(false);
                t.setUpdatedAt(now);
                return trackRepository.save(t);
            })
            .orElseThrow(() -> new IllegalArgumentException("track not found"));
        final PlaylistEntity album = playlistRepository.findById(albumId)
            .filter(p -> !p.isRemoved())
            .orElseThrow(() -> new IllegalArgumentException("album not found"));
        final var existing = trackPlaylistRepository.findFirstByIdPlaylistAndIdTrack(albumId, trackId);
        if (existing.isPresent()) {
            final TrackPlaylistEntity link = existing.get();
            if (link.isRemoved()) {
                link.setRemoved(false);
                link.setAddedAt(now);
                trackPlaylistRepository.save(link);
            }
        } else {
            trackPlaylistRepository.save(
                TrackPlaylistEntity.builder()
                    .idPlaylist(albumId)
                    .idTrack(trackId)
                    .removed(false)
                    .addedAt(now)
                    .build()
            );
        }
        album.setUpdatedAt(now);
        playlistRepository.save(album);
        return mapAlbum(album, artistId, false);
    }

    @Override
    @Transactional
    public AlbumResponseDto patchAlbum(
        final Long artistId,
        final Long albumId,
        final AlbumPatchDto body
    ) {
        if (Objects.isNull(body)) {
            throw new IllegalArgumentException("body required");
        }
        final boolean patchMeta =
            Objects.nonNull(body.getYear()) || !Objects.toString(body.getTitle(), "").isBlank();
        final boolean patchCover = Objects.nonNull(body.getIdCover());
        if (!patchMeta && !patchCover) {
            throw new IllegalArgumentException("year, title or idCover required");
        }
        artistPlaylistRepository
            .findFirstByArtistIdAndPlaylistIdAndDeletedFalse(artistId, albumId)
            .orElseThrow(() -> new IllegalArgumentException("album not linked to artist"));
        final PlaylistEntity album = playlistRepository.findById(albumId)
            .filter(p -> !p.isRemoved())
            .orElseThrow(() -> new IllegalArgumentException("album not found"));
        final LocalDateTime now = LocalDateTime.now();
        if (patchMeta) {
            final String titlePart = resolveTitleForPatch(album.getName(), body.getTitle());
            if (titlePart.isBlank()) {
                throw new IllegalArgumentException("album title required");
            }
            final int yearPart = resolveYearForPatch(album.getName(), body.getYear());
            if (yearPart < 1000 || yearPart > 9999) {
                throw new IllegalArgumentException("year must be four digits");
            }
            album.setName(titlePart + " (" + yearPart + ")");
        }
        if (patchCover) {
            final Long cid = body.getIdCover();
            if (Objects.isNull(cid) || cid <= 0L) {
                throw new IllegalArgumentException("idCover invalid");
            }
            album.setIdCover(cid);
        }
        album.setUpdatedAt(now);
        playlistRepository.save(album);
        return mapAlbum(album, artistId, false);
    }

    private static String resolveTitleForPatch(final String currentName, final String requestedTitle) {
        final String t = Objects.toString(requestedTitle, "").trim();
        if (!t.isEmpty()) {
            return t;
        }
        return albumTitlePart(currentName);
    }

    private static int resolveYearForPatch(final String currentName, final Integer requestedYear) {
        if (requestedYear != null) {
            return requestedYear;
        }
        final Matcher m = ALBUM_NAME_YEAR_SUFFIX.matcher(Objects.toString(currentName, "").trim());
        if (m.matches()) {
            return Integer.parseInt(m.group(2));
        }
        return LocalDate.now().getYear();
    }

    private static String albumTitlePart(final String playlistName) {
        final String s = Objects.toString(playlistName, "").trim();
        if (s.isEmpty()) {
            return "Альбом";
        }
        final Matcher m = ALBUM_NAME_YEAR_SUFFIX.matcher(s);
        if (m.matches()) {
            final String title = m.group(1).trim();
            return title.isEmpty() ? s : title;
        }
        return s;
    }

    private AlbumResponseDto mapAlbum(final PlaylistEntity album, final Long artistId, final boolean deleted) {
        final List<AlbumTrackDto> tracks = new ArrayList<>();
        for (TrackPlaylistEntity link : trackPlaylistRepository.findByIdPlaylistOrderByIdAsc(album.getId())) {
            final boolean artistLinkDeleted = artistTrackRepository
                .findFirstByArtistIdAndTrackId(artistId, link.getIdTrack())
                .map(ArtistTrackEntity::isDeleted)
                .orElse(false);
            final boolean trackDeleted = link.isRemoved() || artistLinkDeleted;
            trackRepository.findById(link.getIdTrack())
                .filter(t -> !t.isRemoved())
                .ifPresent(t -> tracks.add(mapTrack(t, trackDeleted)));
        }
        return AlbumResponseDto.builder()
            .id(album.getId())
            .name(album.getName())
            .idCover(album.getIdCover())
            .artistId(artistId)
            .deleted(deleted)
            .tracks(tracks)
            .build();
    }

    private AlbumTrackDto mapTrack(final TrackEntity track, final boolean deleted) {
        final long duration = track.getDuration() != null ? track.getDuration() : 0L;
        return AlbumTrackDto.builder()
            .id(track.getId())
            .name(track.getName())
            .idCover(track.getIdCover())
            .durationSeconds(duration)
            .deleted(deleted)
            .genreIds(genreIdsForTrack(track.getId()))
            .build();
    }

    private List<Long> genreIdsForTrack(final Long trackId) {
        return trackGenreRepository.findByIdTrackOrderByIdAsc(trackId).stream()
            .map(TrackGenreEntity::getIdGenre)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private void softDeleteArtistTracksForAlbum(
        final Long artistId,
        final Long albumId,
        final LocalDateTime now
    ) {
        for (TrackPlaylistEntity tpl : trackPlaylistRepository.findByIdPlaylistOrderByIdAsc(albumId)) {
            artistTrackRepository.findFirstByArtistIdAndTrackIdAndDeletedFalse(artistId, tpl.getIdTrack())
                .ifPresent(link -> {
                    link.setDeleted(true);
                    link.setDeletedAt(now);
                    artistTrackRepository.save(link);
                });
        }
    }

    private void restoreArtistTracksForAlbum(final Long artistId, final Long albumId) {
        for (TrackPlaylistEntity tpl : trackPlaylistRepository.findByIdPlaylistOrderByIdAsc(albumId)) {
            artistTrackRepository.findFirstByArtistIdAndTrackId(artistId, tpl.getIdTrack()).ifPresent(link -> {
                if (!link.isDeleted()) {
                    return;
                }
                final TrackEntity track = trackRepository.findById(tpl.getIdTrack()).orElse(null);
                if (track == null || track.isRemoved()) {
                    return;
                }
                link.setDeleted(false);
                link.setDeletedAt(null);
                artistTrackRepository.save(link);
            });
        }
    }

    private void finalizeArtistTracksForAlbum(final Long artistId, final Long albumId) {
        final Set<Long> seen = new HashSet<>();
        for (TrackPlaylistEntity tpl : trackPlaylistRepository.findByIdPlaylistOrderByIdAsc(albumId)) {
            if (!seen.add(tpl.getIdTrack())) {
                continue;
            }
            try {
                trackService.finalizeRemoveTrackFromArtist(artistId, tpl.getIdTrack());
            } catch (IllegalArgumentException ignored) {
                // track may lack artist link or already be finalized
            }
        }
    }
}
