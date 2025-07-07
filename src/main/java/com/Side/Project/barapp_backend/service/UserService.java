package com.Side.Project.barapp_backend.service;

import com.Side.Project.barapp_backend.api.models.LoginBody;
import com.Side.Project.barapp_backend.api.models.RegistrationBody;
import com.Side.Project.barapp_backend.exception.UserAlreadyExist;
import com.Side.Project.barapp_backend.models.User;
import com.Side.Project.barapp_backend.models.UserRole;
import com.Side.Project.barapp_backend.dao.UserDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;

    public UserService(UserDAO userDAO, EncryptionService encryptionService, JWTService jwtService) {
        this.userDAO = userDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    /**
     * Register a new user
     */
    public User registerUser(RegistrationBody registrationBody) throws UserAlreadyExist {
        if (userDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExist();
        }

        User user = new User();
        user.setEmail(registrationBody.getEmail());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        user.setRole(UserRole.ROLE_USER); // Default role

        return userDAO.save(user);
    }

    /**
     * Register a barmaker (admin only)
     */
    public User registerBarmaker(RegistrationBody registrationBody) throws UserAlreadyExist {
        if (userDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExist();
        }

        User user = new User();
        user.setEmail(registrationBody.getEmail());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        user.setRole(UserRole.ROLE_BARMAKER);

        return userDAO.save(user);
    }

    /**
     * Login user and return JWT token
     */
    public String loginUser(LoginBody loginBody) {
        Optional<User> optUser = userDAO.findByEmailIgnoreCase(loginBody.getEmail());

        if (optUser.isPresent()) {
            User user = optUser.get();
            if (encryptionService.VerifyPassword(loginBody.getPassword(), user.getPassword())) {
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }

    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        return userDAO.findByEmailIgnoreCase(email);
    }

    /**
     * Get user by ID
     */
    public Optional<User> getUserById(Long id) {
        return userDAO.findById(id);
    }

    /**
     * Get all barmakers
     */
    public List<User> getAllBarmakers() {
        return userDAO.findByRole(UserRole.ROLE_BARMAKER);
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    /**
     * Update user role (admin only)
     */
    public User updateUserRole(Long userId, UserRole newRole) {
        return userDAO.findById(userId)
                .map(user -> {
                    user.setRole(newRole);
                    return userDAO.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Delete user
     */
    public void deleteUser(Long userId) {
        userDAO.deleteById(userId);
    }

    /**
     * Change password
     */
    public User changePassword(Long userId, String newPassword) {
        return userDAO.findById(userId)
                .map(user -> {
                    user.setPassword(encryptionService.encryptPassword(newPassword));
                    return userDAO.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
