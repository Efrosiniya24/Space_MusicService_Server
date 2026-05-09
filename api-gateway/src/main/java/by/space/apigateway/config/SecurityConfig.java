package by.space.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * JWT is validated in each microservice; the gateway only routes traffic.
 * A resource server here caused 401 on {@code /space/**} when a stale {@code Authorization}
 * header was present (e.g. Swagger UI), even for permitAll routes.
 */
@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    SecurityWebFilterChain chain(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors(cors -> {
            })
            .authorizeExchange(ex -> ex.anyExchange().permitAll())
            .build();
    }
}
