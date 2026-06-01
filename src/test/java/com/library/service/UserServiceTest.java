package com.library.service;

import static org.junit.jupiter.api.Assertions.*;

import com.library.model.User;
import com.library.model.UserRole;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the UserService.
 */
// Action 1: Indentation reformatted from 4 to 2 spaces (Google Java Style)
// Action 2: Imports reordered — static first, then com.*, java.*, org.*,
// per Google Java Style Guide import ordering rules.
class UserServiceTest {

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService();
  }

  // --- Add user tests ---

  @Test
  @DisplayName("Should add a user successfully")
  void shouldAddUserSuccessfully() {
    User user = new User("Alice", "alice@test.com",
        UserRole.ADMIN);
    userService.addUser(user);

    Optional<User> found =
        userService.getUserById(user.getId());
    assertTrue(found.isPresent());
    assertEquals("Alice", found.get().getName());
  }

  @Test
  @DisplayName("Should throw exception when adding null user")
  void shouldThrowWhenAddingNullUser() {
    assertThrows(IllegalArgumentException.class,
        () -> userService.addUser(null));
  }

  @Test
  @DisplayName("Should throw exception when adding duplicate user")
  void shouldThrowWhenAddingDuplicateUser() {
    User user = new User("Alice", "alice@test.com",
        UserRole.ADMIN);
    userService.addUser(user);

    assertThrows(IllegalArgumentException.class,
        () -> userService.addUser(user));
  }

  @Test
  @DisplayName("Should throw exception when adding user with duplicate email")
  void shouldThrowWhenAddingUserWithDuplicateEmail() {
    userService.addUser(new User("Alice", "same@test.com",
        UserRole.ADMIN));

    assertThrows(IllegalArgumentException.class,
        () -> userService.addUser(new User("Bob",
            "same@test.com", UserRole.REQUESTER)));
  }

  @Test
  @DisplayName("Should enforce case-insensitive email uniqueness")
  void shouldEnforceCaseInsensitiveEmailUniqueness() {
    userService.addUser(new User("Alice", "Alice@Test.com",
        UserRole.ADMIN));

    assertThrows(IllegalArgumentException.class,
        () -> userService.addUser(new User("Bob",
            "alice@test.com", UserRole.REQUESTER)));
  }

  // --- Get user tests ---

  @Test
  @DisplayName("Should return empty when user not found")
  void shouldReturnEmptyWhenUserNotFound() {
    assertTrue(
        userService.getUserById("fake-id").isEmpty());
  }

  @Test
  @DisplayName("Should return all users")
  void shouldReturnAllUsers() {
    userService.addUser(
        new User("A", "a@test.com", UserRole.ADMIN));
    userService.addUser(
        new User("B", "b@test.com", UserRole.REQUESTER));

    List<User> all = userService.getAllUsers();
    assertEquals(2, all.size());
  }

  @Test
  @DisplayName("Should return empty list when no users exist")
  void shouldReturnEmptyListWhenNoUsersExist() {
    assertTrue(userService.getAllUsers().isEmpty());
  }

  // --- Update user tests ---

  @Test
  @DisplayName("Should update user fields successfully")
  void shouldUpdateUserFieldsSuccessfully() {
    User user = new User("Old", "old@test.com",
        UserRole.ADMIN);
    userService.addUser(user);

    userService.updateUser(user.getId(), "New",
        "new@test.com");

    User updated = userService.getUserById(user.getId())
        .orElseThrow();
    assertEquals("New", updated.getName());
    assertEquals("new@test.com", updated.getEmail());
  }

  @Test
  @DisplayName("Should throw exception when updating non-existent user")
  void shouldThrowWhenUpdatingNonExistentUser() {
    assertThrows(IllegalArgumentException.class,
        () -> userService.updateUser(
            "fake-id", "N", "e@test.com"));
  }

  @Test
  @DisplayName("Should throw exception when updating to a taken email")
  void shouldThrowWhenUpdatingToTakenEmail() {
    User user1 = new User("Alice", "alice@test.com",
        UserRole.ADMIN);
    User user2 = new User("Bob", "bob@test.com",
        UserRole.REQUESTER);
    userService.addUser(user1);
    userService.addUser(user2);

    assertThrows(IllegalArgumentException.class,
        () -> userService.updateUser(user2.getId(),
            "Bob", "alice@test.com"));
  }

  @Test
  @DisplayName("Should allow updating user with their own email")
  void shouldAllowUpdatingUserWithTheirOwnEmail() {
    User user = new User("Alice", "alice@test.com",
        UserRole.ADMIN);
    userService.addUser(user);

    // Updating with the same email should not throw
    assertDoesNotThrow(
        () -> userService.updateUser(user.getId(),
            "Alice Updated", "alice@test.com"));
  }

  // --- Delete user tests ---

  @Test
  @DisplayName("Should delete a user successfully")
  void shouldDeleteUserSuccessfully() {
    User user = new User("Alice", "alice@test.com",
        UserRole.ADMIN);
    userService.addUser(user);

    boolean deleted = userService.deleteUser(user.getId());

    assertTrue(deleted);
    assertTrue(
        userService.getUserById(user.getId()).isEmpty());
  }

  @Test
  @DisplayName("Should return false when deleting non-existent user")
  void shouldReturnFalseWhenDeletingNonExistentUser() {
    assertFalse(userService.deleteUser("fake-id"));
  }
}
