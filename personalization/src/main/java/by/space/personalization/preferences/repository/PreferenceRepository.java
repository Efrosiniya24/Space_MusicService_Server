package by.space.personalization.preferences.repository;

import by.space.personalization.preferences.entity.PreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepository extends JpaRepository<PreferenceEntity, Long> {
}
