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

    /**
     * add additional role, for example, after registration with a different role
     *
     * @param email user email
     * @param role  new role
     * @return updated user
     */
    UserAuthDto addRole(String email, String role);

    /**
     * check if user exists
     *
     * @param email user email
     * @return is user exists
     */
    boolean isUserExist(String email);
}
