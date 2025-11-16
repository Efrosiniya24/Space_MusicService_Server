package by.space.users_service.service;

import by.space.users_service.enums.StatusVenue;
import by.space.users_service.model.dto.VenueAddressDto;
import by.space.users_service.model.dto.VenueDto;

import java.util.List;

public interface VenueService {
    /**
     * method of getting all venues
     *
     * @return list of venues
     */
    List<VenueDto> getAllVenues();

    /**
     * method of getting all venues from bd by status
     *
     * @return list of venues
     */
    List<VenueDto> getAllVenuesByStatus(StatusVenue statusVenue);

    /**
     * method of getting all active venues addresses
     * @param venueId id of venues
     * @return list of addresses
     */
    List<VenueAddressDto> getAllActiveVenueAddresses(Long venueId);
}
