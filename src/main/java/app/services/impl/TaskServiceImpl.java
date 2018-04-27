package app.services.impl;

import app.DAO.TaskDAO;
import app.models.Task;
import app.models.TaskStatus;
import app.models.User;
import app.services.ServiceException;
import app.services.interfaces.TaskService;
import app.services.interfaces.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final static Logger log = LogManager.getLogger(TaskServiceImpl.class);

    @Autowired
    TaskDAO taskDAO;

    @Autowired
    UserService userService;

    @Override
    public void save(Task task) {
        taskDAO.save(task);
    }

    @Override
    public List<Task> findAllByUser(User user) {
        return taskDAO.findAllByUser(user);
    }

    @Override
    public List<Task> findAllNew() {
        return taskDAO.findAllByStatus(TaskStatus.NEW);
    }

    @Override
    public List<Task> findAllAssignedToUser(User user) {
        return taskDAO.findAllByUserAndStatus(user, TaskStatus.ASSIGNED);
    }

    @Override
    public List<Task> findAllFinishedByUser(User user) {
        return taskDAO.findAllByUserAndStatus(user, TaskStatus.FINISHED);
    }

    @Override
    public void addNewTaskManually(Task task) {
        task.setName("Megatask");
        task.setUser(null);
        task.setCreated(LocalDateTime.now());
        task.setFinished(null);
        task.setStatus(TaskStatus.NEW);

        this.save(task);

        log.info("New Task has been created");
    }

    @Override
    public void selectTask(Long taskId) throws ServiceException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        Task task = taskDAO.findById(taskId).orElseThrow(ServiceException::new);

        if (task.getStatus() == TaskStatus.NEW && task.getUser() == null) {
            task.setUser(user);
            task.setStatus(TaskStatus.ASSIGNED);
            save(task);

            log.info(String.format("Task #%s status was changed to assigned", taskId));
        } else {
            throw new ServiceException();
        }
    }

    @Override
    public void rejectTask(Long taskId) throws ServiceException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        Task task = taskDAO.findById(taskId).orElseThrow(ServiceException::new);

        if (task.getStatus() == TaskStatus.ASSIGNED && user.getId().equals(task.getUser().getId())) {
            task.setUser(null);
            task.setStatus(TaskStatus.NEW);
            save(task);

            log.info(String.format("Task #%s status was changed to new", taskId));
        } else {
            throw new ServiceException();
        }
    }

    @Override
    public void finishTask(Long taskId) throws ServiceException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        Task task = taskDAO.findById(taskId).orElseThrow(ServiceException::new);

        if (task.getStatus() == TaskStatus.ASSIGNED && user.getId().equals(task.getUser().getId())) {
            task.setFinished(LocalDateTime.now());
            task.setStatus(TaskStatus.FINISHED);
            save(task);

            log.info(String.format("Task #%s status was changed to finished", taskId));
        } else {
            throw new ServiceException();
        }
    }
}
