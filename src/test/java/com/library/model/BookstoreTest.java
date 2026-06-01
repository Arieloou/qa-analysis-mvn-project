package com.library.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Bookstore model.
 */
// Action 1: Indentation reformatted from 4 to 2 spaces (Google Java Style)
// Action 2: Imports reordered — static imports first, then java.*, then org.*,
// per Google Java Style Guide import ordering rules.
class BookstoreTest {

  private Bookstore bookstore;

  @BeforeEach
  void setUp() {
    bookstore = new Bookstore("Central Library", "123 Main St");
  }

  // --- Construction tests ---

  @Test
  @DisplayName("Should create a valid bookstore")
  void shouldCreateValidBookstore() {
    assertNotNull(bookstore.getId());
    assertEquals("Central Library", bookstore.getName());
    assertEquals("123 Main St", bookstore.getAddress());
    assertTrue(bookstore.getBooks().isEmpty());
  }

  @Test
  @DisplayName("Should throw exception when name is null")
  void shouldThrowWhenNameIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> new Bookstore(null, "Address"));
  }

  @Test
  @DisplayName("Should throw exception when name is blank")
  void shouldThrowWhenNameIsBlank() {
    assertThrows(IllegalArgumentException.class,
        () -> new Bookstore("  ", "Address"));
  }

  @Test
  @DisplayName("Should throw exception when address is null")
  void shouldThrowWhenAddressIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> new Bookstore("Name", null));
  }

  // --- Setter tests ---

  @Test
  @DisplayName("Should update name successfully")
  void shouldUpdateName() {
    bookstore.setName("New Name");
    assertEquals("New Name", bookstore.getName());
  }

  @Test
  @DisplayName("Should throw exception when setting blank name")
  void shouldThrowWhenSettingBlankName() {
    assertThrows(IllegalArgumentException.class,
        () -> bookstore.setName(""));
  }

  @Test
  @DisplayName("Should update address successfully")
  void shouldUpdateAddress() {
    bookstore.setAddress("456 Oak Ave");
    assertEquals("456 Oak Ave", bookstore.getAddress());
  }

  @Test
  @DisplayName("Should throw exception when setting blank address")
  void shouldThrowWhenSettingBlankAddress() {
    assertThrows(IllegalArgumentException.class,
        () -> bookstore.setAddress("  "));
  }

  // --- Book collection tests ---

  @Test
  @DisplayName("Should add a book to the bookstore")
  void shouldAddBook() {
    Book book = new Book("Title", "Author", "123");
    bookstore.addBook(book);

    assertEquals(1, bookstore.getBooks().size());
    assertEquals(book.getId(),
        bookstore.getBooks().get(0).getId());
  }

  @Test
  @DisplayName("Should throw exception when adding null book")
  void shouldThrowWhenAddingNullBook() {
    assertThrows(IllegalArgumentException.class,
        () -> bookstore.addBook(null));
  }

  @Test
  @DisplayName("Should remove a book by ID")
  void shouldRemoveBookById() {
    Book book = new Book("Title", "Author", "123");
    bookstore.addBook(book);

    boolean removed = bookstore.removeBook(book.getId());

    assertTrue(removed);
    assertTrue(bookstore.getBooks().isEmpty());
  }

  @Test
  @DisplayName("Should return false when removing non-existent book")
  void shouldReturnFalseWhenRemovingNonExistentBook() {
    assertFalse(bookstore.removeBook("non-existent-id"));
  }

  @Test
  @DisplayName("Should find a book by ID")
  void shouldFindBookById() {
    Book book = new Book("Title", "Author", "123");
    bookstore.addBook(book);

    Optional<Book> found = bookstore.findBookById(book.getId());

    assertTrue(found.isPresent());
    assertEquals("Title", found.get().getTitle());
  }

  @Test
  @DisplayName("Should return empty when finding non-existent book")
  void shouldReturnEmptyWhenFindingNonExistentBook() {
    Optional<Book> found =
        bookstore.findBookById("non-existent-id");
    assertTrue(found.isEmpty());
  }

  @Test
  @DisplayName("Should return unmodifiable list of books")
  void shouldReturnUnmodifiableBookList() {
    Book book = new Book("Title", "Author", "123");
    bookstore.addBook(book);

    assertThrows(UnsupportedOperationException.class,
        () -> bookstore.getBooks().add(
            new Book("X", "Y", "Z")));
  }

  // --- toString test ---

  @Test
  @DisplayName("Should produce a readable string representation")
  void shouldProduceReadableToString() {
    String result = bookstore.toString();
    assertTrue(result.contains("Central Library"));
    assertTrue(result.contains("bookCount=0"));
  }
}
