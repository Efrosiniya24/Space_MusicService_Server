package by.space.mediacontent.genre.infrastructure.mapper;

import by.space.mediacontent.genre.application.dto.GenreDto;
import by.space.mediacontent.genre.domain.entity.GenreEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreEntity mapToGenreEntity(GenreDto genreDto);

    GenreDto mapToGenreDto(GenreEntity genreEntity);

    List<GenreDto> mapToGenreDtoList(List<GenreEntity> genreEntities);
}
