package by.space.users_service.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import by.space.users_service.mapper.VenueAddressMapper;
import by.space.users_service.model.dto.VenueAddressDto;
import by.space.users_service.model.mysql.venue.address.VenueAddressEntity;
import by.space.users_service.model.mysql.venue.address.VenueAddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {
    @Mock
    private VenueAddressRepository venueAddressRepository;

    @Mock
    private VenueAddressMapper venueAddressMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    void addAddresses_shouldSetVenueIdSaveAndReturnMappedDtos() {
        // given
        Long venueId = 10L;

        List<VenueAddressDto> inputDtos = List.of(new VenueAddressDto(), new VenueAddressDto());

        VenueAddressEntity e1 = new VenueAddressEntity();
        VenueAddressEntity e2 = new VenueAddressEntity();
        List<VenueAddressEntity> mappedEntities = List.of(e1, e2);

        VenueAddressEntity saved1 = new VenueAddressEntity();
        saved1.setVenueId(venueId);
        VenueAddressEntity saved2 = new VenueAddressEntity();
        saved2.setVenueId(venueId);
        List<VenueAddressEntity> savedEntities = List.of(saved1, saved2);

        List<VenueAddressDto> expectedDtos = List.of(new VenueAddressDto(), new VenueAddressDto());

        when(venueAddressMapper.mapToVenueAddressEntity(inputDtos)).thenReturn(mappedEntities);
        when(venueAddressRepository.saveAll(mappedEntities)).thenReturn(savedEntities);
        when(venueAddressMapper.mapToVenueAddressDto(savedEntities)).thenReturn(expectedDtos);

        // when
        List<VenueAddressDto> result = addressService.addAddresses(inputDtos, venueId);

        // then
        assertThat(e1.getVenueId()).isEqualTo(venueId);
        assertThat(e2.getVenueId()).isEqualTo(venueId);

        assertThat(result).isSameAs(expectedDtos);

        verify(venueAddressMapper).mapToVenueAddressEntity(inputDtos);
        verify(venueAddressRepository).saveAll(mappedEntities);
        verify(venueAddressMapper).mapToVenueAddressDto(savedEntities);
        verifyNoMoreInteractions(venueAddressRepository, venueAddressMapper);
    }

    @Test
    void addAddresses_shouldPassEntitiesWithVenueIdToSaveAll() {
        // given
        Long venueId = 77L;
        List<VenueAddressDto> inputDtos = List.of(new VenueAddressDto());

        VenueAddressEntity entity = new VenueAddressEntity();
        when(venueAddressMapper.mapToVenueAddressEntity(inputDtos)).thenReturn(List.of(entity));

        when(venueAddressRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));
        when(venueAddressMapper.mapToVenueAddressDto(anyList())).thenReturn(List.of(new VenueAddressDto()));

        ArgumentCaptor<List<VenueAddressEntity>> captor = ArgumentCaptor.forClass(List.class);

        // when
        addressService.addAddresses(inputDtos, venueId);

        // then
        verify(venueAddressRepository).saveAll(captor.capture());
        List<VenueAddressEntity> passedToSave = captor.getValue();

        assertThat(passedToSave).hasSize(1);
        assertThat(passedToSave.get(0).getVenueId()).isEqualTo(venueId);
    }

    @Test
    void getAllActiveVenueAddresses_shouldFindByVenueIdAndMapToDtos() {
        // given
        Long venueId = 5L;

        List<VenueAddressEntity> entities = List.of(new VenueAddressEntity(), new VenueAddressEntity());
        List<VenueAddressDto> expectedDtos = List.of(new VenueAddressDto(), new VenueAddressDto());

        when(venueAddressRepository.findAllByVenueId(venueId)).thenReturn(entities);
        when(venueAddressMapper.mapToVenueAddressDto(entities)).thenReturn(expectedDtos);

        // when
        List<VenueAddressDto> result = addressService.getAllActiveVenueAddresses(venueId);

        // then
        assertThat(result).isSameAs(expectedDtos);

        verify(venueAddressRepository).findAllByVenueId(venueId);
        verify(venueAddressMapper).mapToVenueAddressDto(entities);
        verifyNoMoreInteractions(venueAddressRepository, venueAddressMapper);
    }
}