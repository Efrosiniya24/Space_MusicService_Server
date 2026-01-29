package by.space.users_service.model.mysql.role;

import by.space.users_service.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    @Query("select id from Authority where role = :role")
    Long findByRole(Role role);
}
