package by.space.users_service.model.mysql.role;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthorityId implements Serializable {
    private Long userId;
    private Long roleId;
}
