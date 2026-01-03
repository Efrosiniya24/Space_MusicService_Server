package by.space.users_service.service.impl;

import by.space.users_service.enums.StatusVenue;
import by.space.users_service.feign.MediaClient;
import by.space.users_service.mapper.VenueMapper;
import by.space.users_service.model.dto.VenueAddressDto;
import by.space.users_service.model.dto.VenueDto;
import by.space.users_service.model.mysql.venue.VenueEntity;
import by.space.users_service.model.mysql.venue.VenueRepository;
import by.space.users_service.model.mysql.venue.address.VenueAddressRepository;
import by.space.users_service.service.AddressService;
import by.space.users_service.service.VenueCuratorService;
import by.space.users_service.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {
    private final VenueRepository venueRepository;
    private final VenueAddressRepository venueAddressRepository;
    private final VenueMapper venueMapper;
    private final VenueCuratorService venueCuratorService;
    private final AddressService addressService;
    private final MediaClient mediaClient;

    @Override
    public List<VenueDto> getAllVenues() {
        final List<VenueDto> venues = getAllVenuesByStatus(StatusVenue.CONFIRMED);
        venues.forEach(venue -> {
            final List<VenueAddressDto> addresses = addressService.getAllActiveVenueAddresses(venue.getId());
            venue.setAddresses(addresses);
        });
        return venues;
    }

    @Override
    public List<VenueDto> getAllVenuesByStatus(final StatusVenue statusVenue) {
        return venueMapper.mapToVenueDto(venueRepository.findAllByStatus(statusVenue));
    }

    @Override
    @Transactional
    public VenueDto createVenue(final VenueDto venueDto) {
        venueDto.setStatus(StatusVenue.PENDING);

        final VenueEntity venue = venueMapper.mapToVenueEntity(venueDto);
        final VenueEntity savedVenue = venueRepository.save(venue);

        final List<VenueAddressDto> addresses = addressService.addAddresses(venueDto.getAddresses(), savedVenue.getId());

        final VenueDto result = venueMapper.mapToVenueDto(savedVenue);
        result.setAddresses(addresses);
        result.setCreatedAt(LocalDateTime.now());

        return result;
    }

    @Override
    public VenueDto getVenue(final Long venueId) {
        return venueMapper.mapToVenueDto(venueRepository.findById(venueId)
            .orElseThrow(() -> new RuntimeException("Venue not found")));
    }

    @Override
    public VenueDto saveVenue(final VenueDto venueDto) {
        final VenueEntity savingVenue = venueRepository.save(venueMapper.mapToVenueEntity(venueDto));
        return venueMapper.mapToVenueDto(savingVenue);
    }

    @Override
    public List<VenueDto> getAllUserVenue(final Long userId) {
        final List<Long> venuesId = venueCuratorService.getAllActiveUserVenue(userId);

        final int size = venuesId.size();
        final List<VenueDto> venues = new ArrayList<>(size);

        for (Long aLong : venuesId) {
            venues.add(getVenue(aLong));
        }

        return venues;
    }
}
