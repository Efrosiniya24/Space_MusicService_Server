package by.space.personalization.preferences.repository;

import by.space.personalization.preferences.entity.PreferencePlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferencePlaylistRepository extends JpaRepository<PreferencePlaylistEntity, Long> {
}
