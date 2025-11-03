package by.space.auth_service.controller;

import by.space.auth_service.model.dto.AuthRequestDto;
import by.space.auth_service.model.dto.RegistrationRequestDto;
import by.space.auth_service.model.dto.ResponseDto;
import by.space.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/singIn")
    public ResponseEntity<ResponseDto> authorize(@RequestBody final AuthRequestDto requestDto){
        return ResponseEntity.ok(authService.authenticate(requestDto));
    }

    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto> signUp(@RequestBody final RegistrationRequestDto request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            SecurityContextHolder.clearContext();
        }
        return ResponseEntity.ok().build();
    }
}
