package app.DAO;
import app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("userRepository")
public interface UserDAO extends JpaRepository<User, Long> {
    User findByUsername(String username);
}