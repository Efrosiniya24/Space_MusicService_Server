package by.space.personalization.preferences.repository;

import by.space.personalization.preferences.entity.PreferenceTrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceTrackRepository extends JpaRepository<PreferenceTrackEntity, Long> {
    List<PreferenceTrackEntity> findByPreferenceId(Long preferenceId);
}
