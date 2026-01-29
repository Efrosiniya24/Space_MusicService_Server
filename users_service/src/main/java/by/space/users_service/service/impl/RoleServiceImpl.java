package by.space.users_service.service.impl;

import by.space.users_service.enums.Role;
import by.space.users_service.mapper.UserAuthorityMapper;
import by.space.users_service.model.dto.UserAuthorityDto;
import by.space.users_service.model.mysql.role.AuthorityRepository;
import by.space.users_service.model.mysql.role.UserAuthority;
import by.space.users_service.model.mysql.role.UserRoleRepository;
import by.space.users_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final UserAuthorityMapper userAuthorityMapper;
    private final AuthorityRepository authorityRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserAuthorityDto setUserAuthority(final Long userId, final Long roleId) {
        final UserAuthority userAuthority = new UserAuthority();
        userAuthority.setUserId(userId);
        userAuthority.setRoleId(roleId);
        return userAuthorityMapper.mapToUserAuthorityDto(userRoleRepository.save(userAuthority));
    }

    @Override
    public Long getRoleId(final Role role) {
        return authorityRepository.findByRole(role);
    }

    @Transactional
    @Override
    public UserAuthorityDto addUserAuthority(final Long userId, final Role role) {
        final Long roleId = getRoleId(role);
        return setUserAuthority(userId, roleId);
    }
}
