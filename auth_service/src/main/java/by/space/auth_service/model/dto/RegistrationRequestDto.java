package by.space.auth_service.model.dto;

import by.space.auth_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDto {
    private String username;
    private String password;
    private String email;
    private Role role;
}
