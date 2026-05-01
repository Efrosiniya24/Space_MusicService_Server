package by.space.personalization.preferences.repository;

import by.space.personalization.preferences.entity.PreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<PreferenceEntity, Long> {
    List<PreferenceEntity> findByVenueIdAndDeletedFalseOrderByCreatedAtDesc(Long venueId);

    List<PreferenceEntity> findByVenueIdAndUserIdAndDeletedFalseOrderByCreatedAtDesc(Long venueId, Long userId);

    Optional<PreferenceEntity> findByIdAndDeletedFalse(Long id);
}
