package by.space.users_service.service.impl;

import by.space.users_service.mapper.VenueAddressMapper;
import by.space.users_service.model.dto.VenueAddressDto;
import by.space.users_service.model.mysql.venue.address.VenueAddressEntity;
import by.space.users_service.model.mysql.venue.address.VenueAddressRepository;
import by.space.users_service.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final VenueAddressRepository venueAddressRepository;
    private final VenueAddressMapper venueAddressMapper;

    @Override
    public List<VenueAddressDto> addAddresses(final List<VenueAddressDto> addresses, final Long venueId) {
        final List<VenueAddressEntity> addressEntity = venueAddressMapper.mapToVenueAddressEntity(addresses);
        addressEntity.forEach(address -> {
            address.setVenueId(venueId);
        });

        final List<VenueAddressEntity> savedAddresses = venueAddressRepository.saveAll(addressEntity);
        return venueAddressMapper.mapToVenueAddressDto(savedAddresses);
    }

    @Override
    public List<VenueAddressDto> getAllActiveVenueAddresses(final Long venueId) {
        return venueAddressMapper.mapToVenueAddressDto(venueAddressRepository.findAllByVenueId(venueId));
    }
}
