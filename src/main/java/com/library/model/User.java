package com.library.model;

import java.util.UUID;

/**
 * Represents a user of the library system.
 * Each user has a role that determines their permissions.
 */
public class User {

    private final String id;
    private String name;
    private String email;
    private UserRole role;

    /**
     * Creates a new user with a generated unique ID.
     *
     * @param name  the user's name (must not be null or blank)
     * @param email the user's email (must not be null or blank)
     * @param role  the user's role (must not be null)
     * @throws IllegalArgumentException if any required field is invalid
     */
    public User(String name, String email, UserRole role) {
        validateNotBlank(name, "Name");
        validateNotBlank(email, "Email");
        if (role == null) {
            throw new IllegalArgumentException("Role must not be null.");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    // --- Setters ---

    public void setName(String name) {
        validateNotBlank(name, "Name");
        this.name = name;
    }

    public void setEmail(String email) {
        validateNotBlank(email, "Email");
        this.email = email;
    }

    public void setRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role must not be null.");
        }
        this.role = role;
    }

    // --- Role helpers ---

    /**
     * Checks whether this user has the ADMIN role.
     *
     * @return true if the user is an administrator
     */
    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    // --- Validation ---

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be null or blank.");
        }
    }

    @Override
    public String toString() {
        return String.format("User{id='%s', name='%s', email='%s', role=%s}",
                id, name, email, role);
    }
}
