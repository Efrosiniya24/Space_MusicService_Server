package by.space.personalization.preferences.repository;

import by.space.personalization.preferences.entity.PreferencePlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferencePlaylistRepository extends JpaRepository<PreferencePlaylistEntity, Long> {
    List<PreferencePlaylistEntity> findByPreferenceId(Long preferenceId);
}
