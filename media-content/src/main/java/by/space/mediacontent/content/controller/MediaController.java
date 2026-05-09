package by.space.mediacontent.content.controller;

import by.space.mediacontent.content.dto.ImageDto;
import by.space.mediacontent.content.dto.VenueCoverStreamDto;
import by.space.mediacontent.content.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class MediaController {
    private final ImageService imageService;

    @PostMapping("/addImage")
    public ResponseEntity<ImageDto> addImage(@RequestParam final MultipartFile file,
                                             @RequestParam final Long ownerId) {
        final ImageDto image = imageService.addImage(file, ownerId);
        return ResponseEntity.ok(image);
    }

    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable final Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/image/{imageId}")
    public ResponseEntity<Resource> getImage(@PathVariable final Long imageId) {
        try {
            final VenueCoverStreamDto stream = imageService.getImageStream(imageId);
            return ResponseEntity.ok()
                .contentType(stream.getMediaType())
                .header(HttpHeaders.CACHE_CONTROL, "private, max-age=300")
                .body(stream.getResource());
        } catch (final NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
