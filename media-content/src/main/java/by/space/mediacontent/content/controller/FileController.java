package by.space.mediacontent.content.controller;

import by.space.mediacontent.content.dto.VenueDocumentDto;
import by.space.mediacontent.content.dto.VenueDocumentStream;
import by.space.mediacontent.content.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/uploadDocuments")
    public ResponseEntity<String> uploadDocuments(
        @RequestParam final Long venueId,
        @RequestParam("files") final List<MultipartFile> files,
        @RequestParam(required = false) final List<Long> venueAddressIds
    ) {
        fileService.uploadDocuments(venueId, files, venueAddressIds);
        return ResponseEntity.ok().body("Documents uploaded");
    }

    @GetMapping("/venueDocuments")
    public ResponseEntity<List<VenueDocumentDto>> listVenueDocuments(
        @RequestParam final Long venueId,
        @RequestParam(required = false) final Long venueAddressId
    ) {
        return ResponseEntity.ok(fileService.listVenueDocuments(venueId, venueAddressId));
    }

    @GetMapping("/venueDocument/download")
    public ResponseEntity<Resource> downloadVenueDocument(@RequestParam final Long documentId) {
        try {
            final VenueDocumentStream stream = fileService.downloadDocument(documentId);
            return ResponseEntity.ok()
                .contentType(stream.getMediaType())
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=\"" + stream.getFilename().replace("\"", "") + "\""
                )
                .body(stream.getResource());
        } catch (final NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
