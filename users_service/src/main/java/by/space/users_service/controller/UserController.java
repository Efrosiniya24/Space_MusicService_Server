package by.space.users_service.controller;

import by.space.users_service.model.dto.RegistrationRequestDto;
import by.space.users_service.model.dto.UserAuthDto;
import by.space.users_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/getUser")
    public ResponseEntity<UserAuthDto> getUser(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUser(email));
    }


    @PostMapping("/makeUser")
    public ResponseEntity<UserAuthDto> makeUser(@RequestBody RegistrationRequestDto requestDto) {
        return ResponseEntity.ok(userService.makeUser(requestDto));
    }
}
