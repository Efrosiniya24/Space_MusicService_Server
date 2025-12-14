package by.space.mediacontent.artist.infrastructure.controller;

import by.space.mediacontent.artist.application.dto.ArtistCreateDto;
import by.space.mediacontent.artist.application.service.ArtistService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/artist")
public class ArtistController {
    private final ArtistService artistService;

    @PostMapping("/create")
    public ResponseEntity<ArtistCreateDto> createArtist(ArtistCreateDto artistCreateDto) {
        return ResponseEntity.ok(artistService.createArtist(artistCreateDto));
    }
}
