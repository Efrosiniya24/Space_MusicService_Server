package by.space.mediacontent.artist.infrastructure.mapper;

import by.space.mediacontent.artist.application.dto.ArtistCreateDto;
import by.space.mediacontent.artist.domain.entity.ArtistEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    ArtistEntity mapToArtistEntity(ArtistCreateDto artistCreateDto);

    ArtistCreateDto mapToArtistCreateDto(ArtistEntity artistEntity);
}
