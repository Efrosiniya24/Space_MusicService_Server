package by.space.users_service.model.mysql.role;

import by.space.users_service.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserAuthority, Long> {
    @Query("SELECT r.role FROM UserAuthority u " +
        "JOIN Authority r ON u.roleId = r.id " +
        "WHERE u.userId = :userId ")
    List<Role> findRoleByUserId(Long userId);

}
