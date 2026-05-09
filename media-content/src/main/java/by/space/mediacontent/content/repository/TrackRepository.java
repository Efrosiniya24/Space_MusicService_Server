package by.space.mediacontent.content.repository;

import by.space.mediacontent.content.domain.entity.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<TrackEntity, Long> {

    List<TrackEntity> findAllByRemovedIsFalseOrderByIdAsc();
}
