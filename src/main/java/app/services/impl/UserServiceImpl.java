package app.services.impl;

import app.DAO.UserDAO;
import app.models.User;
import app.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserDAO userDAO;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
}
