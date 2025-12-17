package by.space.users_service.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import by.space.users_service.model.dto.VenueDto;
import by.space.users_service.service.VenueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VenueManagementServiceImplTest {
    @Mock
    private VenueService venueService;

    @InjectMocks
    private VenueManagementServiceImpl venueManagementService;

    @Test
    void confirmVenue_whenStatusIsInvalid_shouldThrowIllegalArgumentException_andNotSave() {
        // given
        Long venueId = 10L;
        String invalidStatus = "NOT_A_REAL_STATUS";

        VenueDto venueDto = mock(VenueDto.class);
        when(venueService.getVenue(venueId)).thenReturn(venueDto);

        // when / then
        assertThatThrownBy(() -> venueManagementService.confirmVenue(venueId, invalidStatus))
            .isInstanceOf(IllegalArgumentException.class);

        verify(venueService).getVenue(venueId);
        verifyNoMoreInteractions(venueService);

        verifyNoInteractions(venueDto);
    }
}