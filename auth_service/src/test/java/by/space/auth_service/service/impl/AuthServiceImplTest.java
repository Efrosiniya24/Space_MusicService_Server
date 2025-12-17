package by.space.auth_service.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import by.space.auth_service.enums.Role;
import by.space.auth_service.model.dto.AuthRequestDto;
import by.space.auth_service.model.dto.RegistrationRequestDto;
import by.space.auth_service.model.dto.ResponseDto;
import by.space.auth_service.model.dto.UserDto;
import by.space.auth_service.modules.UserClient;
import by.space.auth_service.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserClient userClient;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void authenticate_shouldAuthenticate_fetchUser_generateToken_andReturnResponse() {
        // given
        AuthRequestDto request = mock(AuthRequestDto.class);
        when(request.getEmail()).thenReturn("a@mail.com");
        when(request.getPassword()).thenReturn("pass");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        UserDto user = mock(UserDto.class);
        when(user.getId()).thenReturn(10L);
        when(user.getRoles()).thenReturn(List.of(Role.LISTENER, Role.MUSIC_CURATOR, Role.SYSTEM_ADMIN));

        when(userClient.getUser("a@mail.com")).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn("jwt-token");

        ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor =
            ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        // when
        ResponseDto response = authService.authenticate(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        assertThat(response.getUserId()).isEqualTo(10L);
        assertThat(response.getRoles()).containsExactly(Role.LISTENER, Role.MUSIC_CURATOR, Role.SYSTEM_ADMIN);

        verify(authenticationManager).authenticate(tokenCaptor.capture());
        UsernamePasswordAuthenticationToken captured = tokenCaptor.getValue();
        assertThat(captured.getPrincipal()).isEqualTo("a@mail.com");
        assertThat(captured.getCredentials()).isEqualTo("pass");

        verify(userClient).getUser("a@mail.com");
        verify(jwtService).generateAccessToken(user);

        verifyNoMoreInteractions(authenticationManager, userClient, jwtService);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void signUp_whenRoleProvided_shouldEncodePassword_saveUser_generateToken_andReturnResponse() {
        // given
        RegistrationRequestDto request = mock(RegistrationRequestDto.class);
        when(request.getEmail()).thenReturn("new@mail.com");
        when(request.getPassword()).thenReturn("raw");
        when(request.getRole()).thenReturn(Role.MUSIC_CURATOR);

        when(passwordEncoder.encode("raw")).thenReturn("encoded");

        UserDto savedUser = mock(UserDto.class);
        when(savedUser.getId()).thenReturn(99L);
        when(jwtService.generateAccessToken(savedUser)).thenReturn("jwt");

        ArgumentCaptor<RegistrationRequestDto> regCaptor =
            ArgumentCaptor.forClass(RegistrationRequestDto.class);

        when(userClient.saveUser(any(RegistrationRequestDto.class))).thenReturn(savedUser);

        // when
        ResponseDto response = authService.signUp(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("jwt");
        assertThat(response.getUserId()).isEqualTo(99L);
        assertThat(response.getRoles()).containsExactly(Role.MUSIC_CURATOR);

        verify(passwordEncoder).encode("raw");

        verify(userClient).saveUser(regCaptor.capture());
        RegistrationRequestDto sent = regCaptor.getValue();

        assertThat(sent.getEmail()).isEqualTo("new@mail.com");
        assertThat(sent.getPassword()).isEqualTo("encoded");
        assertThat(sent.getRole()).isEqualTo(Role.MUSIC_CURATOR);

        verify(jwtService).generateAccessToken(savedUser);
        verifyNoMoreInteractions(passwordEncoder, userClient, jwtService);
        verifyNoInteractions(authenticationManager);
    }

    @Test
    void signUp_whenRoleIsNull_shouldDefaultToListener() {
        // given
        RegistrationRequestDto request = mock(RegistrationRequestDto.class);
        when(request.getEmail()).thenReturn("new@mail.com");
        when(request.getPassword()).thenReturn("raw");
        when(request.getRole()).thenReturn(null);

        when(passwordEncoder.encode("raw")).thenReturn("encoded");

        UserDto savedUser = mock(UserDto.class);
        when(savedUser.getId()).thenReturn(7L);
        when(jwtService.generateAccessToken(savedUser)).thenReturn("jwt");

        ArgumentCaptor<RegistrationRequestDto> regCaptor =
            ArgumentCaptor.forClass(RegistrationRequestDto.class);

        when(userClient.saveUser(any(RegistrationRequestDto.class))).thenReturn(savedUser);

        // when
        ResponseDto response = authService.signUp(request);

        // then
        assertThat(response.getRoles()).containsExactly(Role.LISTENER);

        verify(userClient).saveUser(regCaptor.capture());
        RegistrationRequestDto sent = regCaptor.getValue();
        assertThat(sent.getRole()).isEqualTo(Role.LISTENER);
    }

    @Test
    void validateToken_whenExpirationInFuture_shouldReturnTrue() {
        // given
        String token = "t";

        var claims = mock(io.jsonwebtoken.Claims.class);
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 60_000));
        when(jwtService.extractAllClaims(token)).thenReturn(claims);

        // when
        boolean result = authService.validateToken(token);

        // then
        assertThat(result).isTrue();
        verify(jwtService).extractAllClaims(token);
        verify(claims).getExpiration();
    }

    @Test
    void validateToken_whenExpirationInPast_shouldReturnFalse() {
        // given
        String token = "t";

        var claims = mock(io.jsonwebtoken.Claims.class);
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() - 60_000));
        when(jwtService.extractAllClaims(token)).thenReturn(claims);

        // when
        boolean result = authService.validateToken(token);

        // then
        assertThat(result).isFalse();
        verify(jwtService).extractAllClaims(token);
        verify(claims).getExpiration();
    }

    @Test
    void validateToken_whenJwtServiceThrows_shouldReturnFalse() {
        // given
        String token = "bad";
        when(jwtService.extractAllClaims(token)).thenThrow(new RuntimeException("boom"));

        // when
        boolean result = authService.validateToken(token);

        // then
        assertThat(result).isFalse();
        verify(jwtService).extractAllClaims(token);
    }
}
