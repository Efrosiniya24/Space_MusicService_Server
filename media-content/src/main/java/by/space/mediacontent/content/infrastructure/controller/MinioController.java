package by.space.mediacontent.content.infrastructure.controller;

import by.space.mediacontent.content.service.MinioStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MinioController {
    private final MinioStorageService storageService;

    @PostMapping("/upload")
    public String upload(@RequestParam final MultipartFile file) {
        final String objectName = file.getOriginalFilename();
        return storageService.upload(file, objectName);
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(@RequestParam final String key) {
        return ResponseEntity.ok(
            new InputStreamResource(storageService.download(key))
        );
    }
}
