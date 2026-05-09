package by.space.mediacontent.artist.infrastructure.repository;

import by.space.mediacontent.artist.domain.entity.ArtistRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRoleRepository extends JpaRepository<ArtistRoleEntity, Long> {
    List<ArtistRoleEntity> findByArtistIdOrderByIdAsc(Long artistId);

    void deleteByArtistId(Long artistId);
}
