package by.space.personalization.preferences.repository;

import by.space.personalization.preferences.entity.PreferenceGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceGenreRepository extends JpaRepository<PreferenceGenreEntity, Long> {
    List<PreferenceGenreEntity> findByPreferenceId(Long preferenceId);
}
