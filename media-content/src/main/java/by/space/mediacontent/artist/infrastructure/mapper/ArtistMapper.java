package by.space.mediacontent.artist.infrastructure.mapper;

import by.space.mediacontent.artist.application.dto.ArtistCreateDto;
import by.space.mediacontent.artist.domain.entity.ArtistEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ArtistEntity mapToArtistEntity(ArtistCreateDto artistCreateDto);

    @Mapping(target = "cover", ignore = true)
    ArtistCreateDto mapToArtistCreateDto(ArtistEntity artistEntity);
}
