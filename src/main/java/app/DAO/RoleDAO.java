package app.DAO;

import app.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleDAO extends JpaRepository<Role, Long> {
}