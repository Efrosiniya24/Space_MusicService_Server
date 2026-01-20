package by.space.mediacontent.content.repository;

import by.space.mediacontent.content.domain.entity.ImageVenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageVenueRepository extends JpaRepository<ImageVenueEntity, Long> {
}
