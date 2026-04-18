package by.space.users_service.model.mysql.domain.venue.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueAddressRepository extends JpaRepository<VenueAddressEntity, Long> {

    List<VenueAddressEntity> findAllByVenueIdAndDeletedIsFalse(Long venueId);
}
