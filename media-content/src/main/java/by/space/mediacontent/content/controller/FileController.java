package by.space.mediacontent.content.controller;

import by.space.mediacontent.content.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/uploadDocuments")
    public ResponseEntity<String> uploadDocuments(@RequestParam final Long venueId,
                                                  @RequestParam("files") final List<MultipartFile> files) {
        fileService.uploadDocuments(venueId, files);
        return ResponseEntity.ok().body("Documents uploaded");
    }

}
