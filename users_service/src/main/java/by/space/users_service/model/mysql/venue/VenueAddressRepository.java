package by.space.users_service.model.mysql.venue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueAddressRepository extends JpaRepository<VenueAddressEntity, Long> {

    @Query("SELECT ad FROM VenueAddressEntity ad where ad.id = :venueId AND ad.deleted = false")
    List<VenueAddressEntity> findAllByVenueId(Long venueId);
}
