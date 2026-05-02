package by.space.users_service.service.impl;

import by.space.users_service.enums.StatusVenue;
import by.space.users_service.mapper.VenueMapper;
import by.space.users_service.model.dto.VenueAddressDto;
import by.space.users_service.model.dto.VenueCuratorDto;
import by.space.users_service.model.dto.VenueDto;
import by.space.users_service.model.elasticsearch.VenueSearchDocument;
import by.space.users_service.model.elasticsearch.VenueSearchRepository;
import by.space.users_service.model.mysql.domain.venue.VenueEntity;
import by.space.users_service.model.mysql.domain.venue.VenueRepository;
import by.space.users_service.service.AddressService;
import by.space.users_service.service.VenueCuratorService;
import by.space.users_service.service.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;
    private final VenueCuratorService venueCuratorService;
    private final AddressService addressService;
    private final VenueSearchRepository venueSearchRepository;

    @Override
    public List<VenueDto> getAllConfirmedVenues() {
        final List<Long> venueIds =
            addressService.findVenueIdsHavingActiveAddressWithStatus(StatusVenue.CONFIRMED);
        if (venueIds.isEmpty()) {
            return List.of();
        }
        final List<VenueDto> venues = venueMapper.mapToVenueDto(
            venueRepository.findAllById(venueIds).stream().filter(v -> !v.isDeleted()).toList());
        venues.forEach(venue -> venue.setAddresses(
            addressService.getActiveVenueAddressesByStatus(venue.getId(), StatusVenue.CONFIRMED)));
        return venues;
    }

    @Override
    public List<VenueDto> getAllVenues() {
        final List<VenueDto> venues = venueMapper.mapToVenueDto(venueRepository.findAll());
        venues.forEach(venue -> {
            final List<VenueAddressDto> addresses = addressService.getAllActiveVenueAddresses(venue.getId());
            venue.setAddresses(addresses);
        });
        return venues;
    }

    @Override
    public List<VenueDto> searchVenues(final String query) {
        if (Objects.isNull(query) || query.trim().isEmpty()) {
            return getAllVenues();
        }

        final String q = query.trim();
        try {
            final List<VenueSearchDocument> docs =
                venueSearchRepository.findByNameContainingIgnoreCaseAndDeletedFalse(q);
            if (docs.isEmpty()) {
                return List.of();
            }

            final List<Long> ids = docs.stream().map(VenueSearchDocument::getId).toList();
            final Map<Long, Integer> order = new HashMap<>();
            for (int i = 0; i < ids.size(); i++) {
                order.put(ids.get(i), i);
            }

            final List<VenueDto> venues = venueMapper.mapToVenueDto(venueRepository.findAllById(ids)).stream()
                .filter(venue -> !venue.isDeleted())
                .sorted(Comparator.comparingInt(v -> order.getOrDefault(v.getId(), Integer.MAX_VALUE)))
                .collect(Collectors.toList());
            venues.forEach(venue -> venue.setAddresses(addressService.getAllActiveVenueAddresses(venue.getId())));
            return venues;
        } catch (final Exception ex) {
            log.warn("Elasticsearch venue search failed, using DB fallback: {}", ex.toString());
            return searchVenuesFromDb(q);
        }
    }

    private List<VenueDto> searchVenuesFromDb(final String q) {
        final List<VenueDto> venues = venueMapper.mapToVenueDto(
            venueRepository.findByDeletedFalseAndNameContainingIgnoreCase(q));
        venues.sort(Comparator.comparing(VenueDto::getId));
        venues.forEach(venue -> venue.setAddresses(addressService.getAllActiveVenueAddresses(venue.getId())));
        return venues;
    }

    @Override
    public List<VenueDto> getAllVenuesByStatus(final StatusVenue statusVenue) {
        final List<Long> venueIds =
            addressService.findVenueIdsHavingActiveAddressWithStatus(statusVenue);
        if (venueIds.isEmpty()) {
            return List.of();
        }
        final List<VenueDto> venues = venueMapper.mapToVenueDto(venueRepository.findAllById(venueIds));
        venues.forEach(venue -> venue.setAddresses(addressService.getAllActiveVenueAddresses(venue.getId())));
        return venues;
    }

    @Override
    @Transactional
    public VenueDto createVenue(final VenueDto venueDto) {
        venueDto.setCreatedAt(LocalDateTime.now());

        final VenueEntity venue = venueMapper.mapToVenueEntity(venueDto);
        final VenueEntity savedVenue = venueRepository.save(venue);

        final List<VenueAddressDto> addresses = addressService.addAddresses(venueDto.getAddresses(), savedVenue.getId());
        final List<Long> addressesId = addressService.getIdOfAddresses(addresses);

        venueCuratorService.createVenueCurator(
            venueDto.getOwnerId(), savedVenue.getId(), addressesId, true);

        try {
            venueSearchRepository.save(VenueSearchDocument.builder()
                .id(savedVenue.getId())
                .name(savedVenue.getName())
                .deleted(savedVenue.isDeleted())
                .build());
        } catch (final Exception ex) {
            log.warn("Could not index venue in Elasticsearch: {}", ex.toString());
        }

        final VenueDto result = venueMapper.mapToVenueDto(savedVenue);
        result.setAddresses(addresses);
        return result;
    }

    @Override
    public VenueDto getVenue(final Long venueId) {
        final VenueDto venueDto = venueMapper.mapToVenueDto(venueRepository.findById(venueId)
            .orElseThrow(() -> new RuntimeException("Venue not found")));
        venueDto.setAddresses(addressService.getAllActiveVenueAddresses(venueDto.getId()));
        return venueDto;
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
