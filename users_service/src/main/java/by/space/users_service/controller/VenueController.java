package by.space.users_service.controller;

import by.space.users_service.model.dto.VenueDto;
import by.space.users_service.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/venue")
public class VenueController {
    private final VenueService venueService;

    @GetMapping("/allConfirmed")
    public ResponseEntity<List<VenueDto>> getAllConfirmedVenue() {
        return ResponseEntity.ok(venueService.getAllConfirmedVenues());
    }

    @GetMapping("/all")
    public ResponseEntity<List<VenueDto>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }

    @PostMapping("/create")
    public ResponseEntity<VenueDto> createVenue(@RequestBody final VenueDto venueDto) {
        return ResponseEntity.ok(venueService.createVenue(venueDto));
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<VenueDto>> getAllUserVenue(@PathVariable final Long userId) {
        return ResponseEntity.ok(venueService.getAllUserVenue(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDto> getVenueById(@PathVariable final Long id) {
        return ResponseEntity.ok(venueService.getVenue(id));
    }
}
