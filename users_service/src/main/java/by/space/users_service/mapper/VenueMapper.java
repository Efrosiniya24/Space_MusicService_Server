package by.space.users_service.mapper;


import by.space.users_service.model.dto.VenueCuratorDto;
import by.space.users_service.model.dto.VenueDto;
import by.space.users_service.model.mysql.venue.VenueEntity;
import by.space.users_service.model.mysql.venue.curators.VenueCuratorsEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    List<VenueDto> mapToVenueDto(List<VenueEntity> venueEntities);

    VenueEntity mapToVenueEntity(VenueDto venueDto);

    VenueDto mapToVenueDto(VenueEntity venueEntity);

    List<VenueCuratorDto> mapToVenueCuratorDto(List<VenueCuratorsEntity> venueCuratorsEntity);
}
