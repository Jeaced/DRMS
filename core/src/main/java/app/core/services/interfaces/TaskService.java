package app.core.services.interfaces;

import app.core.models.Task;
import app.core.models.User;
import app.core.services.ServiceException;

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
