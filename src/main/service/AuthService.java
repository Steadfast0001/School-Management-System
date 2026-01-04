package service;

import dao.UserDAO;
import model.User;
import utils.PasswordUtil;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();
 public boolean isAdmin(User user) {
        if (user == null || user.getRole() == null) {
            return false;
        }
        String role = user.getRole().trim().toLowerCase();
        return role.equals("admin") || role.equals("superadmin");
    }
    // ✅ REGISTER (STUDENT / TEACHER ONLY)
    public boolean register(String username,
                            String password,
                            String name,
                            String email,
                            String matricule,
                            String level,
                            String role) {

        if (!role.equalsIgnoreCase("Student") &&
            !role.equalsIgnoreCase("Teacher")) {
            return false;
        }

        if (username == null || username.isEmpty() ||
            password == null || password.isEmpty()) {
            return false;
        }

        if (userDAO.existsByUsername(username)) {
            return false;
        }

        String hashedPassword = PasswordUtil.hash(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setName(name);
        user.setEmail(email);
        user.setMatricule(matricule);
        user.setLevel(level);
        user.setRole(role);

        return userDAO.addUser(user);
    }

    // ✅ LOGIN
    public User login(String username, String password) {

        if (username == null || username.isEmpty() ||
            password == null || password.isEmpty()) {
            return null;
        }

        User user = userDAO.findByUsername(username);

        if (user == null) {
            return null;
        }

        boolean valid = PasswordUtil.verify(
                password,
                user.getPassword()
        );

        return valid ? user : null;
    }
}
