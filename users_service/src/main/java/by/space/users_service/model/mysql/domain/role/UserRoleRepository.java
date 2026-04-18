package by.space.users_service.model.mysql.domain.role;

import by.space.users_service.enums.Role;
import by.space.users_service.model.mysql.projection.UserRoleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserAuthority, Long> {
    @Query("SELECT r.role FROM UserAuthority u " +
        "JOIN Authority r ON u.roleId = r.id " +
        "WHERE u.userId = :userId ")
    List<Role> findRoleByUserId(Long userId);

    @Query("SELECT u.userId, r.role FROM UserAuthority u " +
        "JOIN Authority r ON u.roleId = r.id " +
        "WHERE u.userId IN :userIds")
    List<UserRoleProjection> findRolesByUserIds(List<Long> userIds);

}
