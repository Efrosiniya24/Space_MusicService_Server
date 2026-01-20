package by.space.users_service.controller;

import by.space.users_service.model.dto.VenueConfirmDto;
import by.space.users_service.service.VenueManagementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/space/system/venue")
public class VenueManagementController {

    private final VenueManagementService venueManagementService;

    @PostMapping("/confirm/{id}")
    public ResponseEntity<VenueConfirmDto> confirmVenue(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(venueManagementService.confirmVenue(id, status));
    }

}
