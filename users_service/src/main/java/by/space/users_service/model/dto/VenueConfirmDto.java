package by.space.users_service.model.dto;

import by.space.users_service.enums.StatusVenue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VenueConfirmDto {
    private VenueDto venue;
    private StatusVenue status;
}
