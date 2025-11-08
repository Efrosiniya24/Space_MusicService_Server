package by.space.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health","/actuator/info").permitAll()
                .requestMatchers(HttpMethod.POST,
                    "/user/auth/signUp",
                    "/user/auth/signIn",
                    "/user/auth/validate",
                    "/user/auth/logout"
                ).permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
