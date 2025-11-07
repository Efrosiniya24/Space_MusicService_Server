package by.space.users_service.service;

import by.space.users_service.model.dto.RegistrationRequestDto;
import by.space.users_service.model.dto.UserAuthDto;

public interface UserService {
    /**
     * Get main info about user
     *
     * @param email user email
     * @return main info about user
     */
    UserAuthDto getUser(String email);

    /**
     * Save user on db
     *
     * @param userAuthDto user registration data
     * @return savedUser
     */
    UserAuthDto makeUser(RegistrationRequestDto userAuthDto);
}
