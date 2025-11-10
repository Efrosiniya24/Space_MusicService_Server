package by.space.auth_service.config;

import by.space.auth_service.model.dto.UserDto;
import by.space.auth_service.modules.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserClient userClient;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return (final String email) -> {
            final UserDto user = userClient.getUser(email);
            if (user == null) throw new UsernameNotFoundException("User with email " + email + "not found");

            final String[] roles = (user.getRoles() == null ? new String[0]
                : user.getRoles().stream().map(Enum::name).toArray(String[]::new));

            return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(roles)
                .accountExpired(false).accountLocked(false)
                .credentialsExpired(false).disabled(false)
                .build();

        };
    }
}