package by.space.mediacontent.content.repository;

import by.space.mediacontent.content.domain.entity.TrackPlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackPlaylistRepository extends JpaRepository<TrackPlaylistEntity, Long> {

    List<TrackPlaylistEntity> findByIdPlaylistOrderByIdAsc(Long idPlaylist);

    Optional<TrackPlaylistEntity> findFirstByIdPlaylistAndIdTrack(Long idPlaylist, Long idTrack);
}
