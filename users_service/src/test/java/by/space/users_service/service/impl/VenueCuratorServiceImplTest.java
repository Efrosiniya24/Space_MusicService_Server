package by.space.users_service.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import by.space.users_service.mapper.VenueMapper;
import by.space.users_service.model.mysql.venue.curators.VenueCuratorRepository;
import by.space.users_service.model.mysql.venue.curators.VenueCuratorsEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class VenueCuratorServiceImplTest {
    @Mock
    private VenueCuratorRepository venueCuratorRepository;
    @Mock
    private VenueMapper venueMapper;

    @InjectMocks
    private VenueCuratorServiceImpl venueCuratorService;

    @Test
    void getAllActiveUserVenue_shouldReturnVenueIds() {
        // given
        Long userId = 11L;

        VenueCuratorsEntity v1 = mock(VenueCuratorsEntity.class);
        VenueCuratorsEntity v2 = mock(VenueCuratorsEntity.class);

        when(v1.getVenueId()).thenReturn(101L);
        when(v2.getVenueId()).thenReturn(202L);

        when(venueCuratorRepository.findAllByCuratorIdAndDeletedIsFalse(userId))
            .thenReturn(List.of(v1, v2));

        // when
        List<Long> result = venueCuratorService.getAllActiveUserVenue(userId);

        // then
        assertThat(result).containsExactly(101L, 202L);

        verify(venueCuratorRepository).findAllByCuratorIdAndDeletedIsFalse(userId);
        verify(v1).getVenueId();
        verify(v2).getVenueId();

        verifyNoInteractions(venueMapper);
        verifyNoMoreInteractions(venueCuratorRepository, v1, v2);
    }

    @Test
    void getAllActiveUserVenue_whenNoVenues_shouldReturnEmptyList() {
        // given
        Long userId = 99L;

        when(venueCuratorRepository.findAllByCuratorIdAndDeletedIsFalse(userId))
            .thenReturn(Collections.emptyList());

        // when
        List<Long> result = venueCuratorService.getAllActiveUserVenue(userId);

        // then
        assertThat(result).isEmpty();

        verify(venueCuratorRepository).findAllByCuratorIdAndDeletedIsFalse(userId);
        verifyNoInteractions(venueMapper);
        verifyNoMoreInteractions(venueCuratorRepository);
    }
}