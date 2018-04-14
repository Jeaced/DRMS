package app.services.interfaces;

public interface ISecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);
}
