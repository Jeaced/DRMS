package app.DAO;

import app.models.Task;
import app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("taskRepository")
public interface TaskDAO extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);
}