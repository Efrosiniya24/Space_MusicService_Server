package by.space.users_service.mapper;

import by.space.users_service.model.dto.VenueAddressDto;
import by.space.users_service.model.mysql.venue.address.VenueAddressEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VenueAddressMapper {
    List<VenueAddressEntity> mapToVenueAddressEntity(List<VenueAddressDto> venueAddressDto);

    List<VenueAddressDto> mapToVenueAddressDto(List<VenueAddressEntity> venueEntities);
}
