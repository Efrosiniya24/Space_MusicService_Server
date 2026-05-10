package by.space.mediacontent.content.repository;

import by.space.mediacontent.content.domain.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    List<GenreEntity> findAllByDeletedIsFalse();

    Optional<GenreEntity> findFirstByDeletedFalseOrderByIdAsc();

    Optional<GenreEntity> findFirstByDeletedFalseAndNameIgnoreCase(String name);

    Optional<GenreEntity> findFirstByDeletedTrueAndNameIgnoreCase(String name);
}
