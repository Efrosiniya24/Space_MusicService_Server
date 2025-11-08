package by.space.users_service.service.impl;

import by.space.users_service.enums.Role;
import by.space.users_service.mapper.UserMapper;
import by.space.users_service.model.dto.RegistrationRequestDto;
import by.space.users_service.model.dto.UserAuthDto;
import by.space.users_service.model.mysql.role.UserAuthority;
import by.space.users_service.model.mysql.role.UserRoleRepository;
import by.space.users_service.model.mysql.user.UserEntity;
import by.space.users_service.model.mysql.user.UserRepository;
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

    private final Long DEFAULT_ROLE = 1L;

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

        userRoleRepository.save(setUserAuthority(newUser.getId()));

        UserAuthDto userAuthDto = userMapper.mapToUserAuthDto(newUser);
        userAuthDto.setRoles(Collections.singletonList(Role.LISTENER));
        return userAuthDto;
    }

    private UserAuthority setUserAuthority(final Long userId) {
        final UserAuthority userAuthority = new UserAuthority();
        userAuthority.setUserId(userId);
        userAuthority.setRoleId(DEFAULT_ROLE);
        return userAuthority;
    }
}
