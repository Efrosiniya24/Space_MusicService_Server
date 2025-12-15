package by.space.users_service.service;

import by.space.users_service.enums.StatusVenue;
import by.space.users_service.model.dto.VenueAddressDto;
import by.space.users_service.model.dto.VenueDto;
import org.springframework.web.multipart.MultipartFile;

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
     *
     * @param venueId id of venues
     * @return list of addresses
     */
    List<VenueAddressDto> getAllActiveVenueAddresses(Long venueId);

    /**
     * creating of venue
     *
     * @param venueDto venue data
     * @param file     cover of venue
     * @return saved venue data
     */
    VenueDto createVenue(VenueDto venueDto, MultipartFile file);

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
     * @param id user id
     * @return all active user venues
     */
    List<VenueDto> getAllUserVenue(Long id);
}
