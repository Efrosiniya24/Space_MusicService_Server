package by.space.mediacontent.artist.application.service.impl;

import by.space.mediacontent.artist.application.dto.ArtistCreateDto;
import by.space.mediacontent.artist.application.service.ArtistService;
import by.space.mediacontent.artist.application.service.RoleService;
import by.space.mediacontent.artist.domain.entity.ArtistEntity;
import by.space.mediacontent.artist.domain.entity.ArtistRoleEntity;
import by.space.mediacontent.artist.domain.enums.ArtistRole;
import by.space.mediacontent.artist.infrastructure.mapper.ArtistMapper;
import by.space.mediacontent.artist.infrastructure.repository.ArtistRepository;
import by.space.mediacontent.artist.infrastructure.repository.ArtistRoleRepository;
import by.space.mediacontent.artist.infrastructure.search.ArtistSearchDocument;
import by.space.mediacontent.artist.infrastructure.search.ArtistSearchRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistRoleRepository artistRoleRepository;
    private final ArtistMapper artistMapper;
    private final ArtistSearchRepository artistSearchRepository;
    private final RoleService roleService;

    @Override
    @Transactional
    public ArtistCreateDto createArtist(final ArtistCreateDto artistCreateDto) {
        if (Objects.isNull(artistCreateDto.getName()) || artistCreateDto.getName().isBlank()) {
            throw new IllegalArgumentException("name required");
        }
        final ArtistEntity artistToSave = artistMapper.mapToArtistEntity(artistCreateDto);
        artistToSave.setId(null);
        final LocalDateTime now = LocalDateTime.now();
        artistToSave.setCreatedAt(now);
        artistToSave.setUpdatedAt(now);
        artistToSave.setDeleted(false);
        final ArtistEntity artist = artistRepository.save(artistToSave);
        saveArtistRoles(artist.getId(), artistCreateDto.getRoles());
        try {
            artistSearchRepository.save(ArtistSearchDocument.builder()
                .id(artist.getId())
                .name(artist.getName())
                .deleted(artist.isDeleted())
                .build());
        } catch (Exception ex) {
            log.warn("Could not index artist in Elasticsearch: {}", ex.toString());
        }
        return mapArtistDtoWithRoles(artist);
    }

    @Override
    @Transactional
    public ArtistCreateDto updateArtist(final Long id, final ArtistCreateDto artistCreateDto) {
        final ArtistEntity entity = artistRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("artist not found"));
        if (entity.isDeleted()) {
            throw new IllegalArgumentException("artist deleted");
        }
        if (Objects.nonNull(artistCreateDto.getName()) && !artistCreateDto.getName().isBlank()) {
            entity.setName(artistCreateDto.getName().trim());
        }
        if (Objects.nonNull(artistCreateDto.getDescription())) {
            entity.setDescription(artistCreateDto.getDescription().trim().isEmpty()
                ? null
                : artistCreateDto.getDescription().trim());
        }
        entity.setIdCover(artistCreateDto.getIdCover());
        entity.setUpdatedAt(LocalDateTime.now());
        final ArtistEntity saved = artistRepository.save(entity);
        saveArtistRoles(saved.getId(), artistCreateDto.getRoles());
        try {
            artistSearchRepository.save(ArtistSearchDocument.builder()
                .id(saved.getId())
                .name(saved.getName())
                .deleted(saved.isDeleted())
                .build());
        } catch (final Exception ex) {
            log.warn("Could not reindex artist in Elasticsearch: {}", ex.toString());
        }
        return mapArtistDtoWithRoles(saved);
    }

    @Override
    public List<ArtistCreateDto> getAllArtists() {
        return artistRepository.findAll().stream()
            .filter(artist -> !artist.isDeleted())
            .sorted(Comparator.comparing(ArtistEntity::getId))
            .map(this::mapArtistDtoWithRoles)
            .toList();
    }

    @Override
    public ArtistCreateDto getArtistById(final Long id) {
        final ArtistEntity entity = artistRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("artist not found"));
        if (entity.isDeleted()) {
            throw new IllegalArgumentException("artist deleted");
        }
        return mapArtistDtoWithRoles(entity);
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
                .map(this::mapArtistDtoWithRoles)
                .toList();
        } catch (final Exception ex) {
            log.warn("Elasticsearch artist search failed, using DB fallback: {}", ex.toString());
            return artistRepository.findByDeletedFalseAndNameContainingIgnoreCase(q).stream()
                .sorted(Comparator.comparing(ArtistEntity::getId))
                .map(this::mapArtistDtoWithRoles)
                .toList();
        }
    }

    private ArtistCreateDto mapArtistDtoWithRoles(final ArtistEntity entity) {
        final ArtistCreateDto dto = artistMapper.mapToArtistCreateDto(entity);
        dto.setRoles(loadArtistRoles(entity.getId()));
        return dto;
    }

    private List<String> loadArtistRoles(final Long artistId) {
        return artistRoleRepository.findByArtistIdOrderByIdAsc(artistId).stream()
            .map(ArtistRoleEntity::getRoleName)
            .filter(Objects::nonNull)
            .map(roleService::convertArtistRoleToString)
            .filter(s -> s != null && !s.isEmpty())
            .toList();
    }

    private void saveArtistRoles(final Long artistId, final List<String> roles) {
        artistRoleRepository.deleteByArtistId(artistId);
        artistRoleRepository.flush();
        if (Objects.isNull(roles) || roles.isEmpty()) {
            return;
        }
        final Set<ArtistRole> normalized = new LinkedHashSet<>();
        for (final String role : roles) {
            final String cleaned = role == null ? "" : role.trim();
            if (!cleaned.isEmpty()) {
                normalized.add(roleService.convertArtistRole(cleaned));
            }
        }
        if (normalized.isEmpty()) {
            return;
        }
        final List<ArtistRoleEntity> rows = normalized.stream()
            .map(roleEnum -> ArtistRoleEntity.builder()
                .artistId(artistId)
                .roleName(roleEnum)
                .build())
            .toList();
        artistRoleRepository.saveAll(rows);
    }
}
