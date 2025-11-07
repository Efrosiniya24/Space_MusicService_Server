package by.space.users_service.mapper;

import by.space.users_service.model.dto.RegistrationRequestDto;
import by.space.users_service.model.dto.UserAuthDto;
import by.space.users_service.model.mysql.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserAuthDto mapToUserAuthDto(UserEntity userEntity);

    UserEntity mapToUserEntity(RegistrationRequestDto requestDto);
}
