package by.space.auth_service.service;

import by.space.auth_service.model.dto.UserDto;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {
    String generateAccessToken(UserDto user);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    Claims extractAllClaims(String token);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);
}
