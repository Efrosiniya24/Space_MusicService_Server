package by.space.mediacontent.content.controller;

import by.space.mediacontent.content.dto.GenreDto;
import by.space.mediacontent.content.service.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/genre")
public class GenreController {
    private final GenreService genreService;

    @PostMapping("/create")
    public ResponseEntity<GenreDto> createGenre(@RequestBody GenreDto genreDto) {
        return ResponseEntity.ok(genreService.createGenre(genreDto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }
}
