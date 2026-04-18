package by.space.users_service.model.mysql.domain.venue.address;

import by.space.users_service.enums.StatusVenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueAddressRepository extends JpaRepository<VenueAddressEntity, Long> {

    List<VenueAddressEntity> findAllByVenueIdAndDeletedIsFalse(Long venueId);

    List<VenueAddressEntity> findAllByVenueIdAndDeletedIsFalseAndStatus(Long venueId, StatusVenue status);

    long countByStatusAndDeletedFalse(StatusVenue status);

    @Query("SELECT DISTINCT a.venueId FROM VenueAddressEntity a WHERE a.status = :status AND a.deleted = false")
    List<Long> findDistinctVenueIdsByStatus(@Param("status") StatusVenue status);
}