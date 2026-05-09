package by.space.mediacontent.content.repository;

import by.space.mediacontent.content.domain.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
}
