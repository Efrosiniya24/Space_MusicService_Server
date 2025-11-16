package by.space.users_service.service.impl;

import by.space.users_service.enums.StatusVenue;
import by.space.users_service.mapper.VenueMapper;
import by.space.users_service.model.dto.VenueAddressDto;
import by.space.users_service.model.dto.VenueDto;
import by.space.users_service.model.mysql.venue.VenueAddressRepository;
import by.space.users_service.model.mysql.venue.VenueRepository;
import by.space.users_service.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {
    private final VenueRepository venueRepository;
    private final VenueAddressRepository venueAddressRepository;
    private final VenueMapper venueMapper;

    @Override
    public List<VenueDto> getAllVenues() {
        final List<VenueDto> venues = getAllVenuesByStatus(StatusVenue.CONFIRMED);
        venues.forEach(venue -> {
            final List<VenueAddressDto> addresses = getAllActiveVenueAddresses(venue.getId());
            venue.setAddresses(addresses);
        });
        return venues;
    }

    @Override
    public List<VenueDto> getAllVenuesByStatus(final StatusVenue statusVenue) {
        return venueMapper.mapToVenueDto(venueRepository.findAllByStatus(statusVenue));
    }

    @Override
    public List<VenueAddressDto> getAllActiveVenueAddresses(final Long venueId) {
        return venueMapper.mapToVenueAddressDto(venueAddressRepository.findAllByVenueId(venueId));
    }
}
