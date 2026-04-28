package by.space.personalization.preferences.repository;

import by.space.personalization.preferences.entity.PreferenceTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceTimeRepository extends JpaRepository<PreferenceTimeEntity, Long> {

	List<PreferenceTimeEntity> findByBlockIdOrderBySortOrderAsc(Long blockId);
}
