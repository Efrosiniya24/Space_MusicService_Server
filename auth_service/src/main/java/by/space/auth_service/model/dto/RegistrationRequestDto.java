package by.space.auth_service.model.dto;

import by.space.auth_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequestDto {
    private String password;
    private String email;
    private Role role;
    private String repeatPassword;
}
