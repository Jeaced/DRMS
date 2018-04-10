package app.DAO;

import app.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskDAO extends JpaRepository<Task, Long> {
    List<Task> findAllByAssignee_username(String username);
}