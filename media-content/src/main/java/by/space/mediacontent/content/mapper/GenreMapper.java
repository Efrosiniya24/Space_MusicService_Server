package by.space.mediacontent.content.mapper;

import by.space.mediacontent.content.dto.GenreDto;
import by.space.mediacontent.content.domain.entity.GenreEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreEntity mapToGenreEntity(GenreDto genreDto);

    GenreDto mapToGenreDto(GenreEntity genreEntity);

    List<GenreDto> mapToGenreDtoList(List<GenreEntity> genreEntities);
}
