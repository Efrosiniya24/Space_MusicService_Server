package by.space.users_service.model.mysql.projection;

import by.space.users_service.enums.Role;

public interface UserRoleProjection {

    /**
     * Returns user id
     *
     * @return user id
     */
    Long getUserId();

    /**
     * Returns user role
     *
     * @return user id
     */
    Role getRole();
}
