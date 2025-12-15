package by.space.auth_service.modules;

import by.space.auth_service.model.dto.RegistrationRequestDto;
import by.space.auth_service.model.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USERS-SERVICE")
public interface UserClient {

    @PostMapping("/getUser")
    UserDto getUser(@RequestParam("email") String email);

    @PostMapping("/makeUser")
    UserDto saveUser(@RequestBody RegistrationRequestDto userDto);
}
