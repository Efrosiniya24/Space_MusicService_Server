package by.space.users_service.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class SecurityConfig {

    private final DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        BearerTokenResolver bearerTokenResolver = this::resolveBearerToken;
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/getUser",
                    "/makeUser",
                    "/venue/create"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(o -> o
                .jwt(j -> j.decoder(jwtDecoder))
                .bearerTokenResolver(bearerTokenResolver));
        return http.build();
    }

    /**
     * Do not parse JWT for OpenAPI/Swagger (and other public) paths: a bad {@code Authorization}
     * header would otherwise yield 401 before {@code permitAll} is applied.
     */
    private String resolveBearerToken(HttpServletRequest request) {
        if (isJwtFreePath(request)) {
            return null;
        }
        return defaultBearerTokenResolver.resolve(request);
    }

    private static boolean isJwtFreePath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String context = request.getContextPath();
        if (context != null && !context.isEmpty() && path.startsWith(context)) {
            path = path.substring(context.length());
        }
        if (path.isEmpty()) {
            path = "/";
        }
        return path.startsWith("/v3/api-docs")
            || path.startsWith("/swagger-ui")
            || "/swagger-ui.html".equals(path)
            || path.startsWith("/webjars/")
            || "/getUser".equals(path)
            || "/makeUser".equals(path)
            || "/venue/create".equals(path);
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret}") String secretBase64) {
        byte[] key = Base64.getDecoder().decode(secretBase64);
        var secretKey = new SecretKeySpec(key, "HmacSHA256");
        return NimbusJwtDecoder
            .withSecretKey(secretKey)
            .macAlgorithm(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256)
            .build();
    }
}
