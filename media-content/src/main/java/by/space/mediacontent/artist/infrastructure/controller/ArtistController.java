package by.space.mediacontent.artist.infrastructure.controller;

import by.space.mediacontent.artist.application.dto.ArtistCreateDto;
import by.space.mediacontent.artist.application.dto.ArtistEnsureForImportDto;
import by.space.mediacontent.artist.application.service.ArtistService;
import by.space.mediacontent.content.dto.ImageDto;
import by.space.mediacontent.content.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/artist")
public class ArtistController {
    private final ArtistService artistService;
    private final ImageService imageService;

    @PostMapping("/create")
    public ResponseEntity<?> createArtist(@RequestBody final ArtistCreateDto artistCreateDto) {
        try {
            return ResponseEntity.ok(artistService.createArtist(artistCreateDto));
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping(value = "/ensure-for-import", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> ensureArtistForImport(@RequestBody(required = false) final ArtistEnsureForImportDto body) {
        try {
            final String fromTags = body != null ? body.getNameFromTags() : null;
            final String fallback = body != null ? body.getFallbackName() : null;
            return ResponseEntity.ok(artistService.ensureArtistForImport(fromTags, fallback));
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping(value = "/create-with-media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createArtistWithOptionalCover(
        @RequestParam final String name,
        @RequestParam(required = false) final String description,
        @RequestParam(required = false) final List<String> roles,
        @RequestParam(required = false) final Long ownerId,
        @RequestParam(value = "cover", required = false) final MultipartFile cover
    ) {
        try {
            Long idCover = null;
            if (cover != null && !cover.isEmpty()) {
                if (ownerId == null) {
                    return ResponseEntity.badRequest().body("ownerId required when cover is uploaded");
                }
                final ImageDto image = imageService.addImage(cover, ownerId);
                idCover = image.getId();
            }
            final ArtistCreateDto dto = ArtistCreateDto.builder()
                .name(name)
                .description(description)
                .roles(roles)
                .idCover(idCover)
                .build();
            return ResponseEntity.ok(artistService.createArtist(dto));
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping(value = "/{id:\\d+}/with-media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateArtistWithOptionalCover(
        @PathVariable final Long id,
        @RequestParam(required = false) final String name,
        @RequestParam(required = false) final String description,
        @RequestParam(required = false) final List<String> roles,
        @RequestParam(required = false) final Long ownerId,
        @RequestParam(value = "cover", required = false) final MultipartFile cover
    ) {
        try {
            final ArtistCreateDto existing = artistService.getArtistById(id);
            final ArtistCreateDto.ArtistCreateDtoBuilder builder = ArtistCreateDto.builder()
                .name(existing.getName())
                .description(existing.getDescription())
                .idCover(existing.getIdCover())
                .roles(existing.getRoles());
            if (name != null && !name.isBlank()) {
                builder.name(name.trim());
            }
            if (description != null) {
                final String trimmed = description.trim();
                builder.description(trimmed.isEmpty() ? null : trimmed);
            }
            if (roles != null) {
                builder.roles(roles);
            }
            if (cover != null && !cover.isEmpty()) {
                if (ownerId == null) {
                    return ResponseEntity.badRequest().body("ownerId required when cover is uploaded");
                }
                final ImageDto image = imageService.addImage(cover, ownerId);
                builder.idCover(image.getId());
            }
            return ResponseEntity.ok(artistService.updateArtist(id, builder.build()));
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateArtist(
        @PathVariable final Long id,
        @RequestBody final ArtistCreateDto artistCreateDto
    ) {
        try {
            return ResponseEntity.ok(artistService.updateArtist(id, artistCreateDto));
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ArtistCreateDto>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArtistCreateDto>> searchArtists(@RequestParam final String query) {
        return ResponseEntity.ok(artistService.searchArtists(query));
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ArtistCreateDto> getArtistById(@PathVariable final Long id) {
        try {
            return ResponseEntity.ok(artistService.getArtistById(id));
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<?> deleteArtist(@PathVariable final Long id) {
        try {
            artistService.deleteArtist(id);
            return ResponseEntity.noContent().build();
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
