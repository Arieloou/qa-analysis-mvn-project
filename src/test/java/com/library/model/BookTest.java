package com.library.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Book model.
 */
// Action 1: Indentation reformatted from 4 to 2 spaces (Google Java Style)
// Action 2: Imports reordered — static imports first, then regular imports,
// per Google Java Style Guide import ordering rules.
class BookTest {

  // --- Construction tests ---

  @Test
  @DisplayName("Should create a valid book with all fields")
  void shouldCreateValidBook() {
    Book book = new Book("Clean Code", "Robert C. Martin",
        "978-0132350884");

    assertNotNull(book.getId());
    assertEquals("Clean Code", book.getTitle());
    assertEquals("Robert C. Martin", book.getAuthor());
    assertEquals("978-0132350884", book.getIsbn());
    assertTrue(book.isAvailable(),
        "New book should be available by default");
  }

  @Test
  @DisplayName("Should generate unique IDs for different books")
  void shouldGenerateUniqueIds() {
    Book book1 = new Book("Book A", "Author A", "111");
    Book book2 = new Book("Book B", "Author B", "222");

    assertNotEquals(book1.getId(), book2.getId());
  }

  // --- Validation tests ---

  @Test
  @DisplayName("Should throw exception when title is null")
  void shouldThrowWhenTitleIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> new Book(null, "Author", "123"));
  }

  @Test
  @DisplayName("Should throw exception when title is blank")
  void shouldThrowWhenTitleIsBlank() {
    assertThrows(IllegalArgumentException.class,
        () -> new Book("   ", "Author", "123"));
  }

  @Test
  @DisplayName("Should throw exception when author is null")
  void shouldThrowWhenAuthorIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> new Book("Title", null, "123"));
  }

  @Test
  @DisplayName("Should throw exception when ISBN is null")
  void shouldThrowWhenIsbnIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> new Book("Title", "Author", null));
  }

  @Test
  @DisplayName("Should throw exception when ISBN is blank")
  void shouldThrowWhenIsbnIsBlank() {
    assertThrows(IllegalArgumentException.class,
        () -> new Book("Title", "Author", ""));
  }

  // --- Setter tests ---

  @Test
  @DisplayName("Should update title successfully")
  void shouldUpdateTitle() {
    Book book = new Book("Old Title", "Author", "123");
    book.setTitle("New Title");
    assertEquals("New Title", book.getTitle());
  }

  @Test
  @DisplayName("Should throw exception when setting blank title")
  void shouldThrowWhenSettingBlankTitle() {
    Book book = new Book("Title", "Author", "123");
    assertThrows(IllegalArgumentException.class,
        () -> book.setTitle(""));
  }

  @Test
  @DisplayName("Should update author successfully")
  void shouldUpdateAuthor() {
    Book book = new Book("Title", "Old Author", "123");
    book.setAuthor("New Author");
    assertEquals("New Author", book.getAuthor());
  }

  @Test
  @DisplayName("Should throw exception when setting null author")
  void shouldThrowWhenSettingNullAuthor() {
    Book book = new Book("Title", "Author", "123");
    assertThrows(IllegalArgumentException.class,
        () -> book.setAuthor(null));
  }

  @Test
  @DisplayName("Should update ISBN successfully")
  void shouldUpdateIsbn() {
    Book book = new Book("Title", "Author", "old-isbn");
    book.setIsbn("new-isbn");
    assertEquals("new-isbn", book.getIsbn());
  }

  @Test
  @DisplayName("Should toggle availability")
  void shouldToggleAvailability() {
    Book book = new Book("Title", "Author", "123");
    assertTrue(book.isAvailable());

    book.setAvailable(false);
    assertFalse(book.isAvailable());

    book.setAvailable(true);
    assertTrue(book.isAvailable());
  }

  // --- toString test ---

  @Test
  @DisplayName("Should produce a readable string representation")
  void shouldProduceReadableToString() {
    Book book = new Book("Title", "Author", "123");
    String result = book.toString();

    assertTrue(result.contains("Title"));
    assertTrue(result.contains("Author"));
    assertTrue(result.contains("123"));
  }
}
