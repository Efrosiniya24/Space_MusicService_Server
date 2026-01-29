package by.space.users_service.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthorityDto {
    private Long userId;
    private Long roleId;
}
