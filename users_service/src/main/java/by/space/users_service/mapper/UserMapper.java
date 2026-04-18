package by.space.users_service.mapper;

import by.space.users_service.model.dto.RegistrationRequestDto;
import by.space.users_service.model.dto.UserAuthDto;
import by.space.users_service.model.mysql.domain.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    UserAuthDto mapToUserAuthDto(UserEntity userEntity);

    UserEntity mapToUserEntity(RegistrationRequestDto requestDto);

    @Mapping(target = "roles", ignore = true)
    List<UserAuthDto> mapToUserAuthDtoList(List<UserEntity> userEntities);
}
