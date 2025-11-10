package by.space.users_service.model.dto;

import by.space.users_service.enums.GenderType;
import by.space.users_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthDto {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private GenderType gender;
    private LocalDate dateOfBirth;
    private List<Role> roles;
}
