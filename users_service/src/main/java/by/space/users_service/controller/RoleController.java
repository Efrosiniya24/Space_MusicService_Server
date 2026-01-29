package by.space.users_service.controller;

import by.space.users_service.model.dto.UserAuthDto;
import by.space.users_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoleController {
    private final UserService userService;

    @PostMapping("/addRole")
    public ResponseEntity<UserAuthDto> addRole(@RequestParam("email") final String email,
                                               @RequestParam("role") final String role) {
        return ResponseEntity.ok(userService.addRole(email, role));
    }
}
