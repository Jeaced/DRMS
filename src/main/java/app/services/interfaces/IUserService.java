package app.services.interfaces;

import app.models.User;

public interface IUserService {
    void save(User user);

    User findByUsername(String username);
}
