package by.space.mediacontent.artist.infrastructure.controller;

import by.space.mediacontent.artist.application.dto.ArtistCreateDto;
import by.space.mediacontent.artist.application.service.ArtistService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/artist")
public class ArtistController {
    private final ArtistService artistService;

    @PostMapping("/create")
    public ResponseEntity<ArtistCreateDto> createArtist(@RequestBody final ArtistCreateDto artistCreateDto) {
        return ResponseEntity.ok(artistService.createArtist(artistCreateDto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ArtistCreateDto>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArtistCreateDto>> searchArtists(@RequestParam final String query) {
        return ResponseEntity.ok(artistService.searchArtists(query));
    }
}
