package by.space.users_service.mapper;


import by.space.users_service.model.dto.VenueAddressDto;
import by.space.users_service.model.dto.VenueDto;
import by.space.users_service.model.mysql.venue.VenueAddressEntity;
import by.space.users_service.model.mysql.venue.VenueEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    List<VenueDto> mapToVenueDto(List<VenueEntity> venueEntities);

    List<VenueAddressDto> mapToVenueAddressDto(List<VenueAddressEntity> venueEntities);
}
