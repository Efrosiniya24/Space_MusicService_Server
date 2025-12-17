package by.space.auth_service.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import by.space.auth_service.enums.Role;
import by.space.auth_service.model.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();

        ReflectionTestUtils.setField(
            jwtService,
            "SECRET_KEY",
            "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDE="
        );
    }


    @Test
    void generateAccessToken_whenUserRolesNull_shouldDefaultToListenerRole() {
        // given
        UserDto user = mock(UserDto.class);
        when(user.getId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("a@mail.com");
        when(user.getRoles()).thenReturn(null);

        // when
        String token = jwtService.generateAccessToken(user);

        // then
        Claims claims = jwtService.extractAllClaims(token);
        assertThat(claims.get("roles", List.class)).containsExactly("LISTENER");
    }

    @Test
    void extractUsername_shouldReturnSubjectEmail() {
        // given
        UserDto user = mock(UserDto.class);
        when(user.getId()).thenReturn(2L);
        when(user.getEmail()).thenReturn("sub@mail.com");
        when(user.getRoles()).thenReturn(List.of(Role.LISTENER));

        String token = jwtService.generateAccessToken(user);

        // when
        String username = jwtService.extractUsername(token);

        // then
        assertThat(username).isEqualTo("sub@mail.com");
    }

    @Test
    void extractClaim_shouldReturnExpiration() {
        // given
        UserDto user = mock(UserDto.class);
        when(user.getId()).thenReturn(3L);
        when(user.getEmail()).thenReturn("x@mail.com");
        when(user.getRoles()).thenReturn(List.of(Role.LISTENER));

        String token = jwtService.generateAccessToken(user);

        // when
        var exp = jwtService.extractClaim(token, Claims::getExpiration);

        // then
        assertThat(exp).isNotNull();
    }

    @Test
    void extractAllClaims_withInvalidToken_shouldThrowJwtException() {
        // given
        String invalid = "not-a-jwt";

        // when / then
        assertThatThrownBy(() -> jwtService.extractAllClaims(invalid))
            .isInstanceOf(JwtException.class);
    }

    @Test
    void isTokenValid_whenUsernameMatchesAndNotExpired_shouldReturnTrue() {
        // given
        UserDto user = mock(UserDto.class);
        when(user.getId()).thenReturn(4L);
        when(user.getEmail()).thenReturn("valid@mail.com");
        when(user.getRoles()).thenReturn(List.of(Role.LISTENER));

        String token = jwtService.generateAccessToken(user);

        var userDetails = User.withUsername("valid@mail.com")
            .password("x")
            .authorities("ROLE_USER")
            .build();

        // when
        boolean valid = jwtService.isTokenValid(token, userDetails);

        // then
        assertThat(valid).isTrue();
    }

    @Test
    void isTokenValid_whenUsernameDoesNotMatch_shouldReturnFalse() {
        // given
        UserDto user = mock(UserDto.class);
        when(user.getId()).thenReturn(5L);
        when(user.getEmail()).thenReturn("token@mail.com");
        when(user.getRoles()).thenReturn(List.of(Role.LISTENER));

        String token = jwtService.generateAccessToken(user);

        var userDetails = User.withUsername("other@mail.com")
            .password("x")
            .authorities("ROLE_USER")
            .build();

        // when
        boolean valid = jwtService.isTokenValid(token, userDetails);

        // then
        assertThat(valid).isFalse();
    }
}