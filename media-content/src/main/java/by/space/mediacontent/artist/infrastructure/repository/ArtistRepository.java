package by.space.mediacontent.artist.infrastructure.repository;

import by.space.mediacontent.artist.domain.entity.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> {
    List<ArtistEntity> findByDeletedFalseAndNameContainingIgnoreCase(String nameFragment);
}
