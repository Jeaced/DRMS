package app.services;

import app.DAO.TaskDAO;
import app.models.Task;
import app.models.TaskStatus;
import app.models.User;
import app.services.interfaces.ITaskService;
import app.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {
    @Autowired
    TaskDAO taskRepository;

    @Autowired
    IUserService userService;

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
//        this.addNewTask();
        return taskRepository.findAllByStatus(TaskStatus.NEW);
    }

    @Override
    public void addNewTaskManually(String text) {
        Task task = new Task();

        task.setName("Megatask");
        task.setDescription(text);
        task.setUser(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        task.setCreated(LocalDateTime.now());
        task.setFinished(null);
        task.setStatus(TaskStatus.NEW);

        this.save(task);
    }

    @Override
    public void finishTask(String doneId) {
        Task task = find(Long.parseLong(doneId));
        task.setFinished(LocalDateTime.now());
        task.setStatus(TaskStatus.DONE);
        save(task);

    }

    @Override
    public void rejectTask(String rejectId) {
        Task task = find(Long.parseLong(rejectId));
        task.setUser(null);
        task.setStatus(TaskStatus.NEW);
        save(task);
    }

    @Override
    public void selectTask(String selectId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        Task task = find(Long.parseLong(selectId));
        task.setUser(user);
        task.setStatus(TaskStatus.ASSIGNED);
        save(task);

    }

    @Override
    public List<Task> findAll(User user) {
        return taskRepository.findAllByUser(user);
    }
}
