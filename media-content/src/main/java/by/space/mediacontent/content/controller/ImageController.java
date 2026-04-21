package by.space.mediacontent.content.controller;

import by.space.mediacontent.content.dto.VenueCoverStreamDto;
import by.space.mediacontent.content.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/addVenueCover")
    public ResponseEntity<Void> addVenueCover(
        @RequestParam final MultipartFile file,
        @RequestParam final Long ownerId,
        @RequestParam final Long venueId
    ) {
        imageService.addVenueCover(file, ownerId, venueId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getVenueCover")
    public ResponseEntity<Resource> getVenueCover(@RequestParam final Long venueId) {
        try {
            final VenueCoverStreamDto stream = imageService.getVenueCover(venueId);
            return ResponseEntity.ok()
                .contentType(stream.getMediaType())
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                .body(stream.getResource());
        } catch (final NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
