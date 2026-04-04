package by.space.users_service.model.dto;

import by.space.users_service.enums.StatusVenue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueDto {
    private Long id;
    private String name;
    private String email;
    private String cover;
    private String description;
    private String urlWebSite;
    private String phone;
    private StatusVenue status;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long ownerId;
    private List<VenueAddressDto> addresses;
}
