package by.space.users_service.service.impl;

import by.space.users_service.enums.StatusVenue;
import by.space.users_service.mapper.VenueAddressMapper;
import by.space.users_service.model.dto.VenueAddressDto;
import by.space.users_service.model.dto.VenueConfirmDto;
import by.space.users_service.model.dto.VenueDto;
import by.space.users_service.model.mysql.domain.venue.address.VenueAddressEntity;
import by.space.users_service.model.mysql.domain.venue.address.VenueAddressRepository;
import by.space.users_service.service.VenueService;
import by.space.users_service.service.VenueStatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class VenueStatusServiceImpl implements VenueStatusService {

    private final VenueService venueService;
    private final VenueAddressRepository venueAddressRepository;
    private final VenueAddressMapper venueAddressMapper;

    @Override
    public VenueConfirmDto confirmVenue(final Long venueAddressId, final String status) {
        final StatusVenue newStatus = StatusVenue.valueOf(status);
        final VenueAddressEntity address = venueAddressRepository.findById(venueAddressId)
            .orElseThrow(() -> new RuntimeException("Venue address not found"));
        address.setStatus(newStatus);
        address.setUpdatedAt(LocalDateTime.now());
        venueAddressRepository.save(address);

        final VenueDto venue = venueService.getVenue(address.getVenueId());
        final VenueAddressDto addressDto = venueAddressMapper.mapToVenueAddressDto(address);
        return VenueConfirmDto.builder()
            .venue(venue)
            .address(addressDto)
            .status(newStatus)
            .build();
    }

    @Override
    public Long getCountOfVenueByStatus(final StatusVenue statusVenue) {
        return venueAddressRepository.countByStatusAndDeletedFalse(statusVenue);
    }
}
