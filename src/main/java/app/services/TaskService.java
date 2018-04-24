package app.services;

import app.DAO.TaskDAO;
import app.models.Task;
import app.models.User;
import app.services.interfaces.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements ITaskService {

    @Autowired
    TaskDAO taskRepository;

    @Override
    public void save(Task task) {
        taskRepository.save(task);
    }

    @Override
    public List<Task> findAll(User user) {
        return taskRepository.findAllByUser(user);
    }
}
