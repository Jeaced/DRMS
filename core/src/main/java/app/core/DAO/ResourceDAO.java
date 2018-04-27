package app.core.DAO;

import app.core.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("resourceRepository")
public interface ResourceDAO extends JpaRepository<Resource, Long> {
    Resource findByName(String name);
}
