package by.space.mediacontent.content.service;

import by.space.mediacontent.content.domain.enums.GenreSource;
import by.space.mediacontent.content.dto.TrackResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface TrackService {

    TrackResponseDto createTrackForArtist(
        Long artistId,
        Long ownerId,
        String name,
        Long idCover,
        Long durationSeconds,
        List<Long> genreIds,
        MultipartFile file
    );

    List<TrackResponseDto> listTracksByArtist(Long artistId);

    List<TrackResponseDto> listAllTracks();

    TrackResponseDto updateTrackGenres(
        Long artistId,
        Long trackId,
        List<Long> genreIds,
        GenreSource source,
        BigDecimal confidence
    );

    void removeTrackFromArtist(Long artistId, Long trackId);
    void restoreTrackForArtist(Long artistId, Long trackId);
    void finalizeRemoveTrackFromArtist(Long artistId, Long trackId);

    Resource streamTrackAudio(Long trackId);

    MediaType resolveTrackAudioMediaType(Long trackId);
}
