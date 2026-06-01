package com.library.service;

import com.library.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service responsible for CRUD operations on users.
 * Enforces unique email constraint across all users.
 */
public class UserService {

    private final Map<String, User> users;

    public UserService() {
        this.users = new HashMap<>();
    }

    /**
     * Adds a new user to the system.
     * Ensures no duplicate emails exist.
     *
     * @param user the user to add (must not be null)
     * @throws IllegalArgumentException if user is null, already exists, or email is duplicated
     */
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null.");
        }
        if (users.containsKey(user.getId())) {
            throw new IllegalArgumentException("A user with this ID already exists.");
        }
        // Enforce unique email
        boolean emailExists = users.values().stream()
                .anyMatch(existing -> existing.getEmail().equalsIgnoreCase(user.getEmail()));
        if (emailExists) {
            throw new IllegalArgumentException("A user with this email already exists: " + user.getEmail());
        }
        users.put(user.getId(), user);
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the user ID
     * @return an Optional containing the user if found
     */
    public Optional<User> getUserById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    /**
     * Returns all users in the system.
     *
     * @return a list of all users (never null)
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Updates an existing user's fields (name, email).
     * Validates that the new email is not already taken by another user.
     *
     * @param id    the ID of the user to update
     * @param name  the new name
     * @param email the new email
     * @throws IllegalArgumentException if user is not found or email is duplicated
     */
    public void updateUser(String id, String name, String email) {
        User existing = users.get(id);
        if (existing == null) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        // Check email uniqueness (exclude current user)
        boolean emailTaken = users.values().stream()
                .anyMatch(u -> !u.getId().equals(id)
                        && u.getEmail().equalsIgnoreCase(email));
        if (emailTaken) {
            throw new IllegalArgumentException("A user with this email already exists: " + email);
        }
        existing.setName(name);
        existing.setEmail(email);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @return true if the user was found and removed, false otherwise
     */
    public boolean deleteUser(String id) {
        return users.remove(id) != null;
    }
}
