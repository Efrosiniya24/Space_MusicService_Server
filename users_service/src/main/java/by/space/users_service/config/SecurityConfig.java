package by.space.users_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
                .requestMatchers(
                    "/getUser",
                    "/makeUser",
                    "/venue/create"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(o -> o.jwt(j -> j.decoder(jwtDecoder)));
        return http.build();
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

