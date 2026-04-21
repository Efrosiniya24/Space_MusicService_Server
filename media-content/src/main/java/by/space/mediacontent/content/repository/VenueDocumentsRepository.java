package by.space.mediacontent.content.repository;

import by.space.mediacontent.content.domain.entity.DocumentsVenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueDocumentsRepository extends JpaRepository<DocumentsVenueEntity, Long> {

    List<DocumentsVenueEntity> findAllByVenueIdAndRemovedIsFalse(Long venueId);

    List<DocumentsVenueEntity> findAllByVenueIdAndVenueAddressIdAndRemovedIsFalse(
        Long venueId,
        Long venueAddressId
    );
}
