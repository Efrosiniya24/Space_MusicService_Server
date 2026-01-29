package by.space.users_service.service.impl;

import by.space.users_service.enums.Role;
import by.space.users_service.mapper.UserMapper;
import by.space.users_service.model.dto.RegistrationRequestDto;
import by.space.users_service.model.dto.UserAuthDto;
import by.space.users_service.model.mysql.role.AuthorityRepository;
import by.space.users_service.model.mysql.role.UserRoleRepository;
import by.space.users_service.model.mysql.user.UserEntity;
import by.space.users_service.model.mysql.user.UserRepository;
import by.space.users_service.service.RoleService;
import by.space.users_service.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;
    private final AuthorityRepository authorityRepository;
    private final RoleService roleService;

    @Override
    public UserAuthDto getUser(final String email) {
        final UserEntity user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
        final List<Role> roles = userRoleRepository.findRoleByUserId(user.getId());

        final UserAuthDto userAuthDto = userMapper.mapToUserAuthDto(user);
        userAuthDto.setRoles(roles);
        return userAuthDto;
    }

    @Transactional
    @Override
    public UserAuthDto makeUser(final RegistrationRequestDto request) {
        final UserEntity user = UserEntity.builder()
            .email(request.getEmail())
            .password(request.getPassword())
            .createdAt(LocalDateTime.now())
            .build();

        final UserEntity newUser = userRepository.save(user);

        if (isUserExist(request.getEmail())) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }

        roleService.addUserAuthority(newUser.getId(), Role.valueOf(request.getRole()));

        final UserAuthDto userAuthDto = userMapper.mapToUserAuthDto(newUser);
        userAuthDto.setRoles(Collections.singletonList(Role.LISTENER));
        return userAuthDto;
    }

    @Override
    public UserAuthDto addRole(final String email, final String role) {
        final UserAuthDto user = getUser(email);
        final Role newRole = Role.valueOf(role);
        if (user.getRoles().contains(newRole)) {
            throw new RuntimeException("User with email " + email + " is already exists");
        }

        roleService.addUserAuthority(user.getId(), Role.valueOf(role));
        return null;
    }

    @Override
    public boolean isUserExist(final String email) {
        final UserEntity user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }
}
