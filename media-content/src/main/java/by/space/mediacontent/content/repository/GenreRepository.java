package by.space.mediacontent.content.repository;

import by.space.mediacontent.content.domain.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    List<GenreEntity> findAllByDeletedIsFalse();
}
