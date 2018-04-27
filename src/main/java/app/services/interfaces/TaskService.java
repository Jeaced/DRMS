package app.services.interfaces;

import app.models.Task;
import app.models.User;
import app.services.ServiceException;

import java.util.List;

public interface TaskService {
    void save(Task task);

    List<Task> findAllByUser(User user);

    List<Task> findAllNew();

    List<Task> findAllAssignedToUser(User user);

    List<Task> findAllFinishedByUser(User user);

    void addNewTaskManually(Task task);

    void selectTask(Long taskId) throws ServiceException;

    void rejectTask(Long taskId) throws ServiceException;

    void finishTask(Long taskId) throws ServiceException;
}
