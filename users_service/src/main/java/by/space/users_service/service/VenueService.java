package by.space.users_service.service;

import by.space.users_service.enums.StatusVenue;
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
     * creating of venue
     *
     * @param venueDto venue data
     * @return saved venue data
     */
    VenueDto createVenue(VenueDto venueDto);

    /**
     * method for getting venue from db
     *
     * @param venueId venue id
     * @return venue information without address
     */
    VenueDto getVenue(Long venueId);

    /**
     * method for saving venue
     *
     * @param venueDto venue data
     * @return saving venue
     */
    VenueDto saveVenue(VenueDto venueDto);

    /**
     * method for getting all active user venues
     *
     * @param userId user id
     * @return all active user venues
     */
    List<VenueDto> getAllUserVenue(Long userId);

    /**
     * method for getting venues by id
     *
     * @param venuesId venues id
     * @return list of venues
     */
    List<VenueDto> getVenuesById(List<Long> venuesId);
}
