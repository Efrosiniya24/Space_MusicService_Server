package by.space.mediacontent.artist.infrastructure.repository;

import by.space.mediacontent.artist.domain.entity.ArtistPlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistPlaylistRepository extends JpaRepository<ArtistPlaylistEntity, Long> {

    List<ArtistPlaylistEntity> findByArtistIdOrderByIdAsc(Long artistId);

    Optional<ArtistPlaylistEntity> findFirstByArtistIdAndPlaylistId(Long artistId, Long playlistId);

    Optional<ArtistPlaylistEntity> findFirstByArtistIdAndPlaylistIdAndDeletedFalse(Long artistId, Long playlistId);
}
