package by.space.users_service.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VenueAddressDto {
    private Long id;
    private String country;
    private String city;
    private String addressCity;
    private Long venueId;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
