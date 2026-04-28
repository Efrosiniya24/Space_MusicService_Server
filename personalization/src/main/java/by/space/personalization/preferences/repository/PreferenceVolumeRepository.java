package by.space.personalization.preferences.repository;

import by.space.personalization.preferences.entity.PreferenceVolumeEntity;
import by.space.personalization.preferences.enums.VolumeLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceVolumeRepository extends JpaRepository<PreferenceVolumeEntity, Long> {

	List<PreferenceVolumeEntity> findByPreferenceId(Long preferenceId);

	void deleteByPreferenceIdAndVolumeLevel(Long preferenceId, VolumeLevel volumeLevel);
}
