package by.space.users_service.model.mysql.domain.venue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, Long> {
    List<VenueEntity> findByDeletedFalseAndNameContainingIgnoreCase(String nameFragment);
}
