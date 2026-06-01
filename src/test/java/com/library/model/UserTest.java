package com.library.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the User model.
 */
class UserTest {

    // --- Construction tests ---

    @Test
    @DisplayName("Should create a valid admin user")
    void shouldCreateValidAdminUser() {
        User user = new User("Alice", "alice@example.com", UserRole.ADMIN);

        assertNotNull(user.getId());
        assertEquals("Alice", user.getName());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    @DisplayName("Should create a valid requester user")
    void shouldCreateValidRequesterUser() {
        User user = new User("Bob", "bob@example.com", UserRole.REQUESTER);

        assertEquals(UserRole.REQUESTER, user.getRole());
        assertFalse(user.isAdmin());
    }

    @Test
    @DisplayName("Should generate unique IDs for different users")
    void shouldGenerateUniqueIds() {
        User user1 = new User("A", "a@test.com", UserRole.ADMIN);
        User user2 = new User("B", "b@test.com", UserRole.REQUESTER);

        assertNotEquals(user1.getId(), user2.getId());
    }

    // --- Validation tests ---

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new User(null, "email@test.com", UserRole.ADMIN));
    }

    @Test
    @DisplayName("Should throw exception when name is blank")
    void shouldThrowWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("  ", "email@test.com", UserRole.ADMIN));
    }

    @Test
    @DisplayName("Should throw exception when email is null")
    void shouldThrowWhenEmailIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("Name", null, UserRole.ADMIN));
    }

    @Test
    @DisplayName("Should throw exception when email is blank")
    void shouldThrowWhenEmailIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("Name", "", UserRole.ADMIN));
    }

    @Test
    @DisplayName("Should throw exception when role is null")
    void shouldThrowWhenRoleIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("Name", "email@test.com", null));
    }

    // --- Setter tests ---

    @Test
    @DisplayName("Should update name successfully")
    void shouldUpdateName() {
        User user = new User("Old", "email@test.com", UserRole.ADMIN);
        user.setName("New");
        assertEquals("New", user.getName());
    }

    @Test
    @DisplayName("Should throw exception when setting blank name")
    void shouldThrowWhenSettingBlankName() {
        User user = new User("Name", "email@test.com", UserRole.ADMIN);
        assertThrows(IllegalArgumentException.class, () -> user.setName(""));
    }

    @Test
    @DisplayName("Should update email successfully")
    void shouldUpdateEmail() {
        User user = new User("Name", "old@test.com", UserRole.ADMIN);
        user.setEmail("new@test.com");
        assertEquals("new@test.com", user.getEmail());
    }

    @Test
    @DisplayName("Should throw exception when setting null email")
    void shouldThrowWhenSettingNullEmail() {
        User user = new User("Name", "email@test.com", UserRole.ADMIN);
        assertThrows(IllegalArgumentException.class, () -> user.setEmail(null));
    }

    @Test
    @DisplayName("Should update role successfully")
    void shouldUpdateRole() {
        User user = new User("Name", "email@test.com", UserRole.REQUESTER);
        user.setRole(UserRole.ADMIN);
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    @DisplayName("Should throw exception when setting null role")
    void shouldThrowWhenSettingNullRole() {
        User user = new User("Name", "email@test.com", UserRole.ADMIN);
        assertThrows(IllegalArgumentException.class, () -> user.setRole(null));
    }

    // --- Role helper tests ---

    @Test
    @DisplayName("Should return true for admin role check")
    void shouldReturnTrueForAdminRoleCheck() {
        User admin = new User("Admin", "admin@test.com", UserRole.ADMIN);
        assertTrue(admin.isAdmin());
    }

    @Test
    @DisplayName("Should return false for requester role check")
    void shouldReturnFalseForRequesterRoleCheck() {
        User requester = new User("Requester", "req@test.com", UserRole.REQUESTER);
        assertFalse(requester.isAdmin());
    }

    // --- toString test ---

    @Test
    @DisplayName("Should produce a readable string representation")
    void shouldProduceReadableToString() {
        User user = new User("Alice", "alice@test.com", UserRole.ADMIN);
        String result = user.toString();

        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("alice@test.com"));
        assertTrue(result.contains("ADMIN"));
    }
}
