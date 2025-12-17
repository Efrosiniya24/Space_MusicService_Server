package by.space.users_service.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class VenueServiceImplTest {
    @Mock
    private VenueRepository venueRepository;
    @Mock
    private VenueAddressRepository venueAddressRepository;
    @Mock
    private VenueMapper venueMapper;
    @Mock
    private VenueCuratorService venueCuratorService;
    @Mock
    private AddressService addressService;
    @Mock
    private MediaClient mediaClient;

    @InjectMocks
    private VenueServiceImpl venueService;

    @Test
    void getAllVenues_shouldLoadConfirmedAndFillAddressesForEachVenue() {
        // given
        VenueDto v1 = mock(VenueDto.class);
        VenueDto v2 = mock(VenueDto.class);

        when(v1.getId()).thenReturn(1L);
        when(v2.getId()).thenReturn(2L);

        List<VenueDto> confirmedVenues = List.of(v1, v2);

        List<VenueAddressDto> addr1 = List.of(mock(VenueAddressDto.class));
        List<VenueAddressDto> addr2 = List.of(mock(VenueAddressDto.class), mock(VenueAddressDto.class));

        List<VenueEntity> confirmedEntities = List.of(mock(VenueEntity.class), mock(VenueEntity.class));
        when(venueRepository.findAllByStatus(StatusVenue.CONFIRMED)).thenReturn(confirmedEntities);
        when(venueMapper.mapToVenueDto(confirmedEntities)).thenReturn(confirmedVenues);

        when(addressService.getAllActiveVenueAddresses(1L)).thenReturn(addr1);
        when(addressService.getAllActiveVenueAddresses(2L)).thenReturn(addr2);

        // when
        List<VenueDto> result = venueService.getAllVenues();

        // then
        assertThat(result).containsExactly(v1, v2);

        verify(venueRepository).findAllByStatus(StatusVenue.CONFIRMED);
        verify(venueMapper).mapToVenueDto(confirmedEntities);

        verify(addressService).getAllActiveVenueAddresses(1L);
        verify(addressService).getAllActiveVenueAddresses(2L);

        verify(v1).setAddresses(addr1);
        verify(v2).setAddresses(addr2);

        verifyNoInteractions(mediaClient, venueCuratorService);
        // venueAddressRepository тут не должен трогаться
        verifyNoInteractions(venueAddressRepository);

        verifyNoMoreInteractions(venueRepository, venueMapper, addressService, v1, v2);
    }

    @Test
    void getAllVenuesByStatus_shouldMapEntitiesToDtos() {
        // given
        StatusVenue status = StatusVenue.PENDING;

        List<VenueEntity> entities = List.of(mock(VenueEntity.class));
        List<VenueDto> mappedDtos = List.of(mock(VenueDto.class));

        when(venueRepository.findAllByStatus(status)).thenReturn(entities);
        when(venueMapper.mapToVenueDto(entities)).thenReturn(mappedDtos);

        // when
        List<VenueDto> result = venueService.getAllVenuesByStatus(status);

        // then
        assertThat(result).isSameAs(mappedDtos);

        verify(venueRepository).findAllByStatus(status);
        verify(venueMapper).mapToVenueDto(entities);
        verifyNoMoreInteractions(venueRepository, venueMapper);
        verifyNoInteractions(addressService, mediaClient, venueCuratorService, venueAddressRepository);
    }

    @Test
    void createVenue_shouldSetPending_saveVenue_saveAddresses_sendImage_andReturnDtoWithAddresses() {
        // given
        VenueDto inputDto = mock(VenueDto.class);
        MultipartFile image = mock(MultipartFile.class);

        List<VenueAddressDto> inputAddresses = List.of(mock(VenueAddressDto.class));
        when(inputDto.getAddresses()).thenReturn(inputAddresses);
        when(inputDto.getOwnerId()).thenReturn(777L);

        VenueEntity mappedEntity = mock(VenueEntity.class);
        when(venueMapper.mapToVenueEntity(inputDto)).thenReturn(mappedEntity);

        VenueEntity savedEntity = mock(VenueEntity.class);
        when(savedEntity.getId()).thenReturn(55L);
        when(venueRepository.save(mappedEntity)).thenReturn(savedEntity);

        List<VenueAddressDto> savedAddresses = List.of(mock(VenueAddressDto.class), mock(VenueAddressDto.class));
        when(addressService.addAddresses(inputAddresses, 55L)).thenReturn(savedAddresses);

        VenueDto mappedResultDto = mock(VenueDto.class);
        when(venueMapper.mapToVenueDto(savedEntity)).thenReturn(mappedResultDto);

        // when
        VenueDto result = venueService.createVenue(inputDto, image);

        // then
        assertThat(result).isSameAs(mappedResultDto);

        verify(inputDto).setStatus(StatusVenue.PENDING);

        verify(venueMapper).mapToVenueEntity(inputDto);
        verify(venueRepository).save(mappedEntity);

        verify(addressService).addAddresses(inputAddresses, 55L);

        verify(mediaClient).addImage(image, 777L);

        verify(venueMapper).mapToVenueDto(savedEntity);
        verify(mappedResultDto).setAddresses(savedAddresses);

        verifyNoMoreInteractions(venueRepository, venueMapper, addressService, mediaClient, mappedResultDto, inputDto);
        verifyNoInteractions(venueCuratorService, venueAddressRepository);
    }

    @Test
    void getVenue_whenFound_shouldReturnMappedDto() {
        // given
        Long venueId = 10L;

        VenueEntity entity = mock(VenueEntity.class);
        VenueDto dto = mock(VenueDto.class);

        when(venueRepository.findById(venueId)).thenReturn(Optional.of(entity));
        when(venueMapper.mapToVenueDto(entity)).thenReturn(dto);

        // when
        VenueDto result = venueService.getVenue(venueId);

        // then
        assertThat(result).isSameAs(dto);

        verify(venueRepository).findById(venueId);
        verify(venueMapper).mapToVenueDto(entity);

        verifyNoMoreInteractions(venueRepository, venueMapper);
        verifyNoInteractions(addressService, mediaClient, venueCuratorService, venueAddressRepository);
    }

    @Test
    void getVenue_whenNotFound_shouldThrowRuntimeException() {
        // given
        Long venueId = 404L;
        when(venueRepository.findById(venueId)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> venueService.getVenue(venueId))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Venue not found");

        verify(venueRepository).findById(venueId);
        verifyNoMoreInteractions(venueRepository);
        verifyNoInteractions(venueMapper, addressService, mediaClient, venueCuratorService, venueAddressRepository);
    }

    @Test
    void saveVenue_shouldMapToEntitySaveAndMapBackToDto() {
        // given
        VenueDto input = mock(VenueDto.class);

        VenueEntity mappedEntity = mock(VenueEntity.class);
        VenueEntity savedEntity = mock(VenueEntity.class);
        VenueDto output = mock(VenueDto.class);

        when(venueMapper.mapToVenueEntity(input)).thenReturn(mappedEntity);
        when(venueRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(venueMapper.mapToVenueDto(savedEntity)).thenReturn(output);

        // when
        VenueDto result = venueService.saveVenue(input);

        // then
        assertThat(result).isSameAs(output);

        verify(venueMapper).mapToVenueEntity(input);
        verify(venueRepository).save(mappedEntity);
        verify(venueMapper).mapToVenueDto(savedEntity);

        verifyNoMoreInteractions(venueRepository, venueMapper);
        verifyNoInteractions(addressService, mediaClient, venueCuratorService, venueAddressRepository);
    }

    @Test
    void getAllUserVenue_shouldFetchIdsThenLoadEachVenue() {
        // given
        Long userId = 9L;

        when(venueCuratorService.getAllActiveUserVenue(userId)).thenReturn(List.of(2L, 5L));

        VenueEntity e2 = mock(VenueEntity.class);
        VenueEntity e5 = mock(VenueEntity.class);

        VenueDto d2 = mock(VenueDto.class);
        VenueDto d5 = mock(VenueDto.class);

        when(venueRepository.findById(2L)).thenReturn(Optional.of(e2));
        when(venueRepository.findById(5L)).thenReturn(Optional.of(e5));

        when(venueMapper.mapToVenueDto(e2)).thenReturn(d2);
        when(venueMapper.mapToVenueDto(e5)).thenReturn(d5);

        // when
        List<VenueDto> result = venueService.getAllUserVenue(userId);

        // then
        assertThat(result).containsExactly(d2, d5);

        verify(venueCuratorService).getAllActiveUserVenue(userId);

        verify(venueRepository).findById(2L);
        verify(venueRepository).findById(5L);
        verify(venueMapper).mapToVenueDto(e2);
        verify(venueMapper).mapToVenueDto(e5);

        verifyNoInteractions(addressService, mediaClient, venueAddressRepository);
        verifyNoMoreInteractions(venueCuratorService, venueRepository, venueMapper);
    }
}