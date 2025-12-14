package by.space.mediacontent.artist.application.service.impl;

import by.space.mediacontent.artist.application.dto.ArtistCreateDto;
import by.space.mediacontent.artist.application.service.ArtistService;
import by.space.mediacontent.artist.domain.entity.ArtistEntity;
import by.space.mediacontent.artist.infrastructure.mapper.ArtistMapper;
import by.space.mediacontent.artist.infrastructure.repository.ArtistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    @Override
    public ArtistCreateDto createArtist(final ArtistCreateDto artistCreateDto) {
        final ArtistEntity artist = artistRepository.save(artistMapper.mapToArtistEntity(artistCreateDto));
        return artistMapper.mapToArtistCreateDto(artist);
    }
}
