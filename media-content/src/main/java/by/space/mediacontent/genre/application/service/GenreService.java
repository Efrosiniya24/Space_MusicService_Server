package by.space.mediacontent.genre.application.service;

import by.space.mediacontent.genre.application.dto.GenreDto;

import java.util.List;

public interface GenreService {
    /**
     * method for creation genre
     *
     * @param genreDto genre data
     * @return saving genre data
     */
    GenreDto createGenre(GenreDto genreDto);

    /**
     * method for getting all genres
     *
     * @return list of all genres
     */
    List<GenreDto> getAllGenres();
}
