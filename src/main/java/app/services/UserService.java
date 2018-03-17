package app.services;

import app.models.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
