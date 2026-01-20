package by.space.auth_service.service.impl;

import by.space.auth_service.enums.Role;
import by.space.auth_service.model.dto.AuthRequestDto;
import by.space.auth_service.model.dto.RegistrationRequestDto;
import by.space.auth_service.model.dto.ResponseDto;
import by.space.auth_service.model.dto.UserDto;
import by.space.auth_service.modules.UserClient;
import by.space.auth_service.service.AuthService;
import by.space.auth_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserClient userClient;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public ResponseDto authenticate(final AuthRequestDto request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        final UserDto user = userClient.getUser(request.getEmail());
        final String token = jwtService.generateAccessToken(user);

        return ResponseDto.builder()
            .accessToken(token)
            .userId(user.getId())
            .roles(user.getRoles())
            .build();
    }

    @Override
    public ResponseDto signUp(final RegistrationRequestDto request) {
        if (!checkPassword(request.getPassword(), request.getRepeatPassword())) {
            throw new IllegalStateException("Passwords don't match");
        }

        final Role role = request.getRole() != null ? request.getRole() : Role.LISTENER;
        final RegistrationRequestDto user = RegistrationRequestDto
            .builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(role)
            .build();

        final UserDto userDto = userClient.saveUser(user);
        final String token = jwtService.generateAccessToken(userDto);

        return ResponseDto.builder()
            .accessToken(token)
            .userId(userDto.getId())
            .roles(Collections.singletonList(user.getRole()))
            .build();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            var claims = jwtService.extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkPassword(final String firstPassword, final String secondPassword) {
        return Objects.equals(firstPassword, secondPassword);
    }
}
