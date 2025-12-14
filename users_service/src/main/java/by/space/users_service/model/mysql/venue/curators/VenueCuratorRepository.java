package by.space.users_service.model.mysql.venue.curators;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueCuratorRepository extends JpaRepository<VenueCuratorsEntity, Long> {

    @Query("SELECT vc FROM VenueCuratorsEntity vc " +
        "JOIN VenueEntity v ON vc.id = v.id " +
        "WHERE vc.deleted = false AND v.deleted = false AND v.status = 'CONFIRMED' ")
    List<VenueCuratorsEntity> findAllByCuratorIdAndDeletedIsFalse(Long id);
}
