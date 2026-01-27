package by.space.users_service.service.impl;

import by.space.users_service.enums.StatusVenue;
import by.space.users_service.mapper.VenueMapper;
import by.space.users_service.model.dto.VenueAddressDto;
import by.space.users_service.model.dto.VenueCuratorDto;
import by.space.users_service.model.dto.VenueDto;
import by.space.users_service.model.mysql.venue.VenueEntity;
import by.space.users_service.model.mysql.venue.VenueRepository;
import by.space.users_service.service.AddressService;
import by.space.users_service.service.VenueCuratorService;
import by.space.users_service.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;
    private final VenueCuratorService venueCuratorService;
    private final AddressService addressService;

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
        venueDto.setCreatedAt(LocalDateTime.now());

        final VenueEntity venue = venueMapper.mapToVenueEntity(venueDto);
        final VenueEntity savedVenue = venueRepository.save(venue);

        final List<VenueAddressDto> addresses = addressService.addAddresses(venueDto.getAddresses(), savedVenue.getId());
        final List<Long> addressesId = addressService.getIdOfAddresses(addresses);

        venueCuratorService.createVenueCurator(
            venueDto.getOwnerId(), savedVenue.getId(), addressesId, true);

        final VenueDto result = venueMapper.mapToVenueDto(savedVenue);
        result.setAddresses(addresses);
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
        final List<VenueCuratorDto> curatorVenues = venueCuratorService.getAllActiveUserVenue(userId);
        if (curatorVenues.isEmpty()) {
            return List.of();
        }

        final List<Long> venueId = curatorVenues.stream()
            .map(VenueCuratorDto::getVenueId)
            .distinct()
            .collect(Collectors.toList());
        final List<VenueDto> venues = getVenuesById(venueId);
        final Map<Long, VenueDto> venueMap = venues.stream()
            .peek(v -> v.setAddresses(new ArrayList<>()))
            .collect(Collectors.toMap(VenueDto::getId, v -> v));

        final List<Long> addressesId = curatorVenues.stream()
            .map(VenueCuratorDto::getAddressId)
            .distinct()
            .toList();
        final List<VenueAddressDto> address = addressService.getAllAddressesByIds(addressesId);
        final Map<Long, VenueAddressDto> addressMap = address.stream()
            .collect(Collectors.toMap(VenueAddressDto::getId, v -> v));

        curatorVenues.forEach(venueCuratorDto -> {
            final VenueDto venueDto = venueMap.get(venueCuratorDto.getVenueId());
            final VenueAddressDto addressDto = addressMap.get(venueCuratorDto.getAddressId());

            venueDto.getAddresses().add(addressDto);
        });

        return venues;
    }

    @Override
    public List<VenueDto> getVenuesById(final List<Long> venuesId) {
        return venueMapper.mapToVenueDto(venueRepository.findAllById(venuesId));
    }
}
