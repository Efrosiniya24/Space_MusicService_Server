package by.space.mediacontent.artist.application.service;

import by.space.mediacontent.artist.application.dto.ArtistCreateDto;

import java.util.List;

public interface ArtistService {
    /**
     * method for creation artist
     *
     * @param artistCreateDto data for creation artist
     * @return saving artist
     */
    ArtistCreateDto createArtist(ArtistCreateDto artistCreateDto);

    List<ArtistCreateDto> getAllArtists();

    List<ArtistCreateDto> searchArtists(String query);
}
