package app.services;

import app.DAO.TaskDAO;
import app.models.Task;
import app.models.TaskStatus;
import app.models.User;
import app.services.interfaces.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {
    @Autowired
    TaskDAO taskRepository;

    @Override
    public void save(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task find(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        return task.orElse(new Task());
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findAllNew() {
        this.addNewTask();
        return taskRepository.findAllByStatus(TaskStatus.NEW);
    }

    @Override
    public List<Task> findAll(User user) {
        return taskRepository.findAllByUser(user);
    }

    private void addNewTask() {
        /*
         * Temporary solution
         */
        // TODO: tasks should be added from sensor system
        Task task = new Task();

        task.setName("Megatask");
        task.setDescription("DO THIS !");
        task.setUser(null);
        task.setCreated(LocalDateTime.now());
        task.setFinished(null);
        task.setStatus(TaskStatus.NEW);

        this.save(task);
    }
}
