package app.services.interfaces;


import app.models.Task;
import app.models.User;

import java.util.List;

public interface ITaskService {
    void save(Task task);

    Task find(Long id);

    List<Task> findAll();

    List<Task> findAll(User user);

    List<Task> findAllNew();
}
