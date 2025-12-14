package by.space.mediacontent.genre.application.service.impl;

import by.space.mediacontent.genre.application.dto.GenreDto;
import by.space.mediacontent.genre.application.service.GenreService;
import by.space.mediacontent.genre.domain.entity.GenreEntity;
import by.space.mediacontent.genre.infrastructure.mapper.GenreMapper;
import by.space.mediacontent.genre.infrastructure.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public GenreDto createGenre(final GenreDto genreDto) {
        GenreEntity genreEntity = genreRepository.save(genreMapper.mapToGenreEntity(genreDto));
        return genreMapper.mapToGenreDto(genreEntity);
    }

    @Override
    public List<GenreDto> getAllGenres() {
        return genreMapper.mapToGenreDtoList(genreRepository.findAllByDeletedIsFalse());
    }
}
