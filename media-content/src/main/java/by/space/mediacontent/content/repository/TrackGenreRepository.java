package by.space.mediacontent.content.repository;

import by.space.mediacontent.content.domain.entity.TrackGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackGenreRepository extends JpaRepository<TrackGenreEntity, Long> {

    List<TrackGenreEntity> findByIdTrackOrderByIdAsc(Long idTrack);

    void deleteByIdTrack(Long idTrack);
}
