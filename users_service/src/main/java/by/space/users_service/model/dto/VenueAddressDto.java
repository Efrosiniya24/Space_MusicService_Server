package by.space.users_service.model.dto;

import by.space.users_service.enums.StatusVenue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VenueAddressDto {
    private Long id;
    private String country;
    private String city;
    private String addressCity;
    private Long venueId;
    private StatusVenue status;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
