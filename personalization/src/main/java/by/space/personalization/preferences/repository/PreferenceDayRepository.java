package by.space.personalization.preferences.repository;

import by.space.personalization.preferences.entity.PreferenceDayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceDayRepository extends JpaRepository<PreferenceDayEntity, Long> {

	List<PreferenceDayEntity> findByBlockIdOrderByWeekdayAsc(Long blockId);
}
