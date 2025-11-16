package by.space.users_service.controller;

import by.space.users_service.model.dto.VenueDto;
import by.space.users_service.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/venue")
public class VenueController {
    private final VenueService venueService;

    @GetMapping
    public ResponseEntity<List<VenueDto>> getAllVenue() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }
}
