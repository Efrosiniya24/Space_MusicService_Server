package by.space.personalization.preferences.repository;

import by.space.personalization.preferences.entity.PreferenceScheduleBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceScheduleBlockRepository extends JpaRepository<PreferenceScheduleBlockEntity, Long> {

	List<PreferenceScheduleBlockEntity> findByPreferenceIdOrderBySortOrderAsc(Long preferenceId);
}
