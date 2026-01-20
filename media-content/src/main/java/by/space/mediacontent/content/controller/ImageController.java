package by.space.mediacontent.content.controller;

import by.space.mediacontent.content.dto.ImageDto;
import by.space.mediacontent.content.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/addVenueCover")
    public ResponseEntity<ImageDto> addImage(@RequestParam final MultipartFile file,
                                             @RequestParam final Long ownerId,
                                             @RequestParam final Long venueId) {
        imageService.addVenueCover(file, ownerId, venueId);
        return ResponseEntity.ok().build();
    }


}
