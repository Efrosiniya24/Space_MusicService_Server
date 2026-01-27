package by.space.users_service.mapper;

import by.space.users_service.model.dto.VenueCuratorDto;
import by.space.users_service.model.mysql.venue.curators.VenueCuratorsEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VenueCuratorMapper {
    List<VenueCuratorDto> mapToVenueCuratorDto(List<VenueCuratorsEntity> venueCuratorsEntities);

    List<VenueCuratorsEntity> mapToVenueCuratorEntity(List<VenueCuratorDto> venueCuratorsDto);
}
