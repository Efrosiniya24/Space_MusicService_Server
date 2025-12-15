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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/venue")
public class VenueController {
    private final VenueService venueService;

    @GetMapping("/all")
    public ResponseEntity<List<VenueDto>> getAllVenue() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }

    @PostMapping("/create")
    public ResponseEntity<VenueDto> createVenue(@RequestBody final VenueDto venueDto,
                                                @RequestParam final MultipartFile file) {
        return ResponseEntity.ok(venueService.createVenue(venueDto, file));
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<VenueDto>> getAllUserVenue(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getAllUserVenue(id));
    }
}
