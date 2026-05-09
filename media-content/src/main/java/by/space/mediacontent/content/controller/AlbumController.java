package by.space.mediacontent.content.controller;

import by.space.mediacontent.content.dto.AlbumCreateDto;
import by.space.mediacontent.content.dto.AlbumPatchDto;
import by.space.mediacontent.content.dto.AlbumResponseDto;
import by.space.mediacontent.content.service.AlbumService;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/album")
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping("/for-artist/{artistId}")
    public ResponseEntity<?> createForArtist(
        @PathVariable final Long artistId,
        @RequestBody final AlbumCreateDto request
    ) {
        try {
            return ResponseEntity.ok(albumService.createAlbumForArtist(artistId, request));
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/by-artist/{artistId:\\d+}")
    public ResponseEntity<List<AlbumResponseDto>> listByArtist(@PathVariable final Long artistId) {
        return ResponseEntity.ok(albumService.listAlbumsByArtist(artistId));
    }

    @PostMapping("/{albumId:\\d+}/tracks")
    public ResponseEntity<?> addTrackToAlbum(
        @PathVariable final Long albumId,
        @RequestParam("artistId") final Long artistId,
        @RequestParam("trackId") final Long trackId
    ) {
        try {
            return ResponseEntity.ok(albumService.addTrackToAlbum(artistId, albumId, trackId));
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PatchMapping(value = "/{albumId:\\d+}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patchAlbum(
        @PathVariable final Long albumId,
        @RequestParam("artistId") final Long artistId,
        @RequestBody final AlbumPatchDto body
    ) {
        try {
            return ResponseEntity.ok(albumService.patchAlbum(artistId, albumId, body));
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{albumId:\\d+}")
    public ResponseEntity<Void> removeFromArtist(
        @PathVariable final Long albumId,
        @RequestParam("artistId") final Long artistId
    ) {
        try {
            albumService.removeAlbumFromArtist(artistId, albumId);
            return ResponseEntity.noContent().build();
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{albumId:\\d+}/restore")
    public ResponseEntity<Void> restoreForArtist(
        @PathVariable final Long albumId,
        @RequestParam("artistId") final Long artistId
    ) {
        try {
            albumService.restoreAlbumForArtist(artistId, albumId);
            return ResponseEntity.noContent().build();
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{albumId:\\d+}/final")
    public ResponseEntity<Void> finalizeRemoveFromArtist(
        @PathVariable final Long albumId,
        @RequestParam("artistId") final Long artistId
    ) {
        try {
            albumService.finalizeRemoveAlbumFromArtist(artistId, albumId);
            return ResponseEntity.noContent().build();
        } catch (final IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
