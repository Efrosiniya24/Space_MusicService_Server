package by.space.mediacontent.artist.application.service;

import by.space.mediacontent.artist.application.dto.ArtistCreateDto;

public interface ArtistService {
    /**
     * method for creation artist
     *
     * @param artistCreateDto data for creation artist
     * @return saving artist
     */
    ArtistCreateDto createArtist(ArtistCreateDto artistCreateDto);
}
