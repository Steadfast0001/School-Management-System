package service;

import dao.UserDAO;
import model.User;
import utils.PasswordUtil;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    /**
     * Authenticate user credentials
     */
    public User login(String username, String password) {

        if (username == null || password == null ||
            username.isEmpty() || password.isEmpty()) {
            return null;
        }

        User user = userDAO.findByUsername(username);

        if (user == null) {
            System.out.println("User not found: " + username);
            return null;
        }

        if (PasswordUtil.verify(password, user.getPassword())) {
            System.out.println("Login successful for: " + username);
            return user;
        } else {
            System.out.println("Password verification failed for: " + username);
            return null;
        }
    }

    /**
     * Register a new user
     */
    public boolean register(String username, String password, String name, String email, String matricule, String level) {
        if (username == null || password == null || name == null || email == null ||
            username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
            return false;
        }

        // Check if username already exists
        if (userDAO.findByUsername(username) != null) {
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(PasswordUtil.hash(password));
        user.setRole("USER"); // Default role
        user.setName(name);
        user.setEmail(email);
        user.setMatricule(matricule);
        user.setLevel(level);

        return userDAO.addUser(user);
    }

    /**
     * Check if user has a specific role
     */
    public boolean hasRole(User user, String role) {
        return user != null && role.equalsIgnoreCase(user.getRole());
    }

    /**
     * Check if user is admin
     */
    public boolean isAdmin(User user) {
        return hasRole(user, "ADMIN");
    }

    /**
     * Hash password
     */
    public String hashPassword(String password) {
        return PasswordUtil.hash(password);
    }
}
