package by.space.mediacontent.artist.application.service.impl;

import by.space.mediacontent.artist.application.dto.ArtistCreateDto;
import by.space.mediacontent.artist.application.service.ArtistService;
import by.space.mediacontent.artist.domain.entity.ArtistEntity;
import by.space.mediacontent.artist.infrastructure.mapper.ArtistMapper;
import by.space.mediacontent.artist.infrastructure.repository.ArtistRepository;
import by.space.mediacontent.artist.infrastructure.search.ArtistSearchDocument;
import by.space.mediacontent.artist.infrastructure.search.ArtistSearchRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;
    private final ArtistSearchRepository artistSearchRepository;

    @Override
    public ArtistCreateDto createArtist(final ArtistCreateDto artistCreateDto) {
        final ArtistEntity artistToSave = artistMapper.mapToArtistEntity(artistCreateDto);
        artistToSave.setId(null);
        final LocalDateTime now = LocalDateTime.now();
        artistToSave.setCreatedAt(now);
        artistToSave.setUpdatedAt(now);
        artistToSave.setDeleted(false);
        final ArtistEntity artist = artistRepository.save(artistToSave);
        try {
            artistSearchRepository.save(ArtistSearchDocument.builder()
                .id(artist.getId())
                .name(artist.getName())
                .deleted(artist.isDeleted())
                .build());
        } catch (Exception ex) {
            log.warn("Could not index artist in Elasticsearch: {}", ex.toString());
        }
        return artistMapper.mapToArtistCreateDto(artist);
    }

    @Override
    public List<ArtistCreateDto> getAllArtists() {
        return artistRepository.findAll().stream()
            .filter(artist -> !artist.isDeleted())
            .sorted(Comparator.comparing(ArtistEntity::getId))
            .map(artistMapper::mapToArtistCreateDto)
            .toList();
    }

    @Override
    public List<ArtistCreateDto> searchArtists(final String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllArtists();
        }

        final String q = query.trim();
        try {
            final List<ArtistSearchDocument> docs =
                artistSearchRepository.findByNameContainingIgnoreCaseAndDeletedFalse(q);
            if (docs.isEmpty()) {
                return List.of();
            }

            final List<Long> ids = docs.stream().map(ArtistSearchDocument::getId).toList();
            final Map<Long, Integer> order = new HashMap<>();
            for (int i = 0; i < ids.size(); i++) {
                order.put(ids.get(i), i);
            }

            return artistRepository.findAllById(ids).stream()
                .filter(artist -> !artist.isDeleted())
                .sorted(Comparator.comparingInt(a -> order.getOrDefault(a.getId(), Integer.MAX_VALUE)))
                .map(artistMapper::mapToArtistCreateDto)
                .toList();
        } catch (Exception ex) {
            log.warn("Elasticsearch artist search failed, using DB fallback: {}", ex.toString());
            return artistRepository.findByDeletedFalseAndNameContainingIgnoreCase(q).stream()
                .sorted(Comparator.comparing(ArtistEntity::getId))
                .map(artistMapper::mapToArtistCreateDto)
                .toList();
        }
    }
}
