package by.space.users_service.service.impl;

import by.space.users_service.enums.StatusVenue;
import by.space.users_service.model.dto.VenueConfirmDto;
import by.space.users_service.model.dto.VenueDto;
import by.space.users_service.service.VenueManagementService;
import by.space.users_service.service.VenueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class VenueManagementServiceImpl implements VenueManagementService {

    private final VenueService venueService;

    @Override
    public VenueConfirmDto confirmVenue(final Long id, final String status) {
        final VenueDto venue = venueService.getVenue(id);
        venue.setStatus(StatusVenue.valueOf(status));
        venue.setUpdatedAt(LocalDateTime.now());

        venueService.saveVenue(venue);
        return VenueConfirmDto.builder()
            .venue(venue)
            .status(StatusVenue.valueOf(status))
            .build();
    }
}
