package by.space.users_service.service;

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
}
