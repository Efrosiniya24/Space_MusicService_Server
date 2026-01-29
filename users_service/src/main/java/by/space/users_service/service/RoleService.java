package by.space.users_service.service;

import by.space.users_service.enums.Role;
import by.space.users_service.model.dto.UserAuthorityDto;

public interface RoleService {
    /**
     * set user authority in userAuthority table
     *
     * @param userId user id
     * @param roleId role id
     * @return user authority
     */
    UserAuthorityDto setUserAuthority(final Long userId, final Long roleId);

    /**
     * get role id
     *
     * @param role name of role
     * @return role id
     */
    Long getRoleId(final Role role);

    /**
     * add user role
     *
     * @param userId user id
     * @param role   role name
     * @return user role
     */
    UserAuthorityDto addUserAuthority(final Long userId, final Role role);

}
