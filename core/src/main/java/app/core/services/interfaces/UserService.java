package app.core.services.interfaces;

import app.core.models.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
