package by.space.mediacontent.content.service;

import by.space.mediacontent.content.dto.GenreDto;

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
