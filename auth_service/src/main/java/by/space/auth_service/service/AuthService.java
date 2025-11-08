package by.space.auth_service.service;

import by.space.auth_service.model.dto.AuthRequestDto;
import by.space.auth_service.model.dto.RegistrationRequestDto;
import by.space.auth_service.model.dto.ResponseDto;

public interface AuthService {
    /**
     * Generation of the token
     *
     * @param authRequestDto details of user
     * @return details of authorization
     */
    ResponseDto authenticate(AuthRequestDto authRequestDto);

    /**
     * Make a user
     *
     * @param request details of user
     * @return token with details about user
     */
    ResponseDto signUp(RegistrationRequestDto request);

    boolean validateToken(String token);
}
