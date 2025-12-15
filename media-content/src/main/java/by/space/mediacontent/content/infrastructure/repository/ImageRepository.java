package by.space.mediacontent.content.infrastructure.repository;

import by.space.mediacontent.content.domain.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
