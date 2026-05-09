package by.space.mediacontent.content.service.impl;

import by.space.mediacontent.content.mapper.GenreMapper;
import by.space.mediacontent.content.repository.GenreRepository;
import by.space.mediacontent.content.dto.GenreDto;
import by.space.mediacontent.content.service.GenreService;
import by.space.mediacontent.content.domain.entity.GenreEntity;
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
