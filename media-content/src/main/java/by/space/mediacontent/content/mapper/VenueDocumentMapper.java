package by.space.mediacontent.content.mapper;

import by.space.mediacontent.content.domain.entity.DocumentsVenueEntity;
import by.space.mediacontent.content.dto.VenueDocumentDto;
import org.springframework.stereotype.Component;

@Component
public class VenueDocumentMapper {

    public VenueDocumentDto toDto(final DocumentsVenueEntity e) {
        if (e == null) {
            return null;
        }
        return VenueDocumentDto.builder()
            .id(e.getId())
            .venueId(e.getVenueId())
            .venueAddressId(e.getVenueAddressId())
            .title(e.getTitle())
            .build();
    }
}
