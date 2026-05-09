package by.space.mediacontent.artist.infrastructure.repository;

import by.space.mediacontent.artist.domain.entity.ArtistTrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistTrackRepository extends JpaRepository<ArtistTrackEntity, Long> {

    List<ArtistTrackEntity> findByArtistIdAndDeletedFalse(Long artistId);
    List<ArtistTrackEntity> findByArtistIdOrderByIdAsc(Long artistId);

    Optional<ArtistTrackEntity> findFirstByArtistIdAndTrackIdAndDeletedFalse(
        Long artistId,
        Long trackId
    );

    Optional<ArtistTrackEntity> findFirstByArtistIdAndTrackId(Long artistId, Long trackId);

    List<ArtistTrackEntity> findByTrackIdAndDeletedFalseOrderByIdAsc(Long trackId);
}
