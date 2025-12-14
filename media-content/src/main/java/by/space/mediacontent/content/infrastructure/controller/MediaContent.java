package by.space.mediacontent.content.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MediaContent {

    @PostMapping
    public ResponseEntity<Void> addMedia() {
        return ResponseEntity.ok().build();
    }
}
