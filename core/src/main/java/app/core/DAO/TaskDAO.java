package app.core.DAO;

import app.core.models.Task;
import app.core.models.TaskStatus;
import app.core.models.TaskType;
import app.core.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("taskRepository")
public interface TaskDAO extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);

    List<Task> findAllByStatus(TaskStatus status);

    List<Task> findAllByUserAndStatus(User user, TaskStatus status);

    List<Task> findAllByType(TaskType type);
}