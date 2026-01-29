package by.space.users_service.mapper;

import by.space.users_service.model.dto.UserAuthorityDto;
import by.space.users_service.model.mysql.role.UserAuthority;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAuthorityMapper {
    UserAuthorityDto mapToUserAuthorityDto(UserAuthority userAuthority);
}
