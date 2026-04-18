package by.space.users_service.service;

import by.space.users_service.enums.StatusVenue;
import by.space.users_service.model.dto.VenueAddressDto;

import java.util.List;

public interface AddressService {
    /**
     * method for saving addresses of venue in db
     *
     * @param addresses addresses of venue
     * @param venueId   id of venue
     * @return saving addresses
     */
    List<VenueAddressDto> addAddresses(List<VenueAddressDto> addresses, Long venueId);

    /**
     * get all addresses of the venue
     *
     * @param venueId id of the venue
     * @return all venue addresses
     */
    List<VenueAddressDto> getAllActiveVenueAddresses(final Long venueId);

    /**
     * Active venue addresses for a venue filtered by moderation status.
     *
     * @param venueId id of the venue
     * @param status  address status
     * @return matching active addresses
     */
    List<VenueAddressDto> getActiveVenueAddressesByStatus(Long venueId, StatusVenue status);

    /**
     * Venue ids that have at least one active address in the given status.
     *
     * @param status address status
     * @return distinct venue ids
     */
    List<Long> findVenueIdsHavingActiveAddressWithStatus(StatusVenue status);

    /**
     * getting id of venue addresses
     *
     * @param addresses venue addresses
     * @return id of venue addresses
     */
    List<Long> getIdOfAddresses(List<VenueAddressDto> addresses);

    /**
     * getting all addresses by ids
     *
     * @param addressesIds addresses ids
     * @return addresses dto
     */
    List<VenueAddressDto> getAllAddressesByIds(List<Long> addressesIds);
}
