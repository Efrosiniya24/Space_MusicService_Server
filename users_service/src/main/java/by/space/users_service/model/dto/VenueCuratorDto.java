package by.space.users_service.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VenueCuratorDto {
    private Long id;
    private Long curatorId;
    private Long venueId;
    private Long addressId;
    private Boolean userOwner;
    private boolean deleted;

}
