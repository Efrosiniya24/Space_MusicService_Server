package by.space.personalization.preferences.repository;

import by.space.personalization.preferences.entity.PreferenceScheduleDateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceScheduleDateRepository extends JpaRepository<PreferenceScheduleDateEntity, Long> {

	List<PreferenceScheduleDateEntity> findByBlockIdOrderBySpecificDateAsc(Long blockId);
}
