package by.space.users_service.model.mysql.venue;

import by.space.users_service.enums.StatusVenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, Long> {
    List<VenueEntity> findAllByStatus(final StatusVenue status);
}
