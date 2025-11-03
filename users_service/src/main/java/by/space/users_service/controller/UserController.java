package by.space.users_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @PostMapping("/getUSer")
    public ResponseEntity<UserDto> getUser(@RequestParam String username) {
        return authService.authenticate(requestDto);
    }
}
