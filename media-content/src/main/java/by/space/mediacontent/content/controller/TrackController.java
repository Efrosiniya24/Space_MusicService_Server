package by.space.mediacontent.content.controller;

import by.space.mediacontent.content.dto.ImageDto;
import by.space.mediacontent.content.dto.TrackGenresPatchDto;
import by.space.mediacontent.content.dto.TrackResponseDto;
import by.space.mediacontent.content.service.AudioMetadataProbeService;
import by.space.mediacontent.content.service.ImageService;
import by.space.mediacontent.content.service.TrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/track")
public class TrackController {

    private final TrackService trackService;
    private final AudioMetadataProbeService audioMetadataProbeService;
    private final ImageService imageService;

    @PostMapping(value = "/probe-metadata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> probeMetadata(@RequestParam("file") final MultipartFile file) {
        try {
            return ResponseEntity.ok(audioMetadataProbeService.probeUploadedFile(file));
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.warn("probe-metadata interrupted");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                "Чтение метаданных прервано."
            );
        } catch (final IOException ex) {
            log.warn("probe-metadata failed: {}", ex.toString());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                "Не удалось прочитать метаданные (нужен ffprobe из FFmpeg в PATH на сервере): "
                    + ex.getMessage()
            );
        }
    }

    @PostMapping(value = "/for-artist/{artistId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createForArtist(
        @PathVariable final Long artistId,
        @RequestParam("ownerId") final Long ownerId,
        @RequestParam("name") final String name,
        @RequestParam(value = "idCover", required = false) final Long idCover,
        @RequestParam(value = "cover", required = false) final MultipartFile cover,
        @RequestParam(value = "durationSeconds", required = false) final Long durationSeconds,
        @RequestParam(value = "genreIds", required = false) final List<Long> genreIds,
        @RequestParam("file") final MultipartFile file
    ) {
        try {
            Long resolvedCoverId = idCover;
            if (cover != null && !cover.isEmpty()) {
                final ImageDto coverImage = imageService.addImage(cover, ownerId);
                resolvedCoverId = coverImage.getId();
            }
            return ResponseEntity.ok(trackService.createTrackForArtist(
                artistId, ownerId, name, resolvedCoverId, durationSeconds, genreIds, file));
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/by-artist/{artistId:\\d+}")
    public ResponseEntity<List<TrackResponseDto>> listByArtist(@PathVariable final Long artistId) {
        return ResponseEntity.ok(trackService.listTracksByArtist(artistId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TrackResponseDto>> listAll() {
        return ResponseEntity.ok(trackService.listAllTracks());
    }

    @PatchMapping(value = "/{trackId:\\d+}/genres", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patchGenres(
        @PathVariable final Long trackId,
        @RequestParam("artistId") final Long artistId,
        @RequestBody(required = false) final TrackGenresPatchDto body
    ) {
        try {
            final List<Long> ids = body != null ? body.getGenreIds() : null;
            return ResponseEntity.ok(trackService.updateTrackGenres(
                artistId,
                trackId,
                ids,
                body != null ? body.getSource() : null,
                body != null ? body.getConfidence() : null
            ));
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{trackId:\\d+}")
    public ResponseEntity<Void> removeFromArtist(
        @PathVariable final Long trackId,
        @RequestParam("artistId") final Long artistId
    ) {
        try {
            trackService.removeTrackFromArtist(artistId, trackId);
            return ResponseEntity.noContent().build();
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{trackId:\\d+}/restore")
    public ResponseEntity<Void> restoreForArtist(
        @PathVariable final Long trackId,
        @RequestParam("artistId") final Long artistId
    ) {
        try {
            trackService.restoreTrackForArtist(artistId, trackId);
            return ResponseEntity.noContent().build();
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{trackId:\\d+}/final")
    public ResponseEntity<Void> finalizeRemoveFromArtist(
        @PathVariable final Long trackId,
        @RequestParam("artistId") final Long artistId
    ) {
        try {
            trackService.finalizeRemoveTrackFromArtist(artistId, trackId);
            return ResponseEntity.noContent().build();
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{trackId:\\d+}/audio")
    public ResponseEntity<Resource> streamAudio(@PathVariable final Long trackId) {
        try {
            final Resource body = trackService.streamTrackAudio(trackId);
            final MediaType mediaType = trackService.resolveTrackAudioMediaType(trackId);
            return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CACHE_CONTROL, "private, max-age=120")
                .body(body);
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
