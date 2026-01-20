package by.space.users_service.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public Long getUserId() {
        final Jwt jwt = (Jwt) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        return jwt.getClaim("userId");
    }
}
