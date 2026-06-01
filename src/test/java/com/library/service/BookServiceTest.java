package com.library.service;

import com.library.model.Book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the BookService.
 */
class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
    }

    // --- Add book tests ---

    @Test
    @DisplayName("Should add a book successfully")
    void shouldAddBookSuccessfully() {
        Book book = new Book("Clean Code", "Robert Martin", "978-0132350884");
        bookService.addBook(book);

        Optional<Book> found = bookService.getBookById(book.getId());
        assertTrue(found.isPresent());
        assertEquals("Clean Code", found.get().getTitle());
    }

    @Test
    @DisplayName("Should throw exception when adding null book")
    void shouldThrowWhenAddingNullBook() {
        assertThrows(IllegalArgumentException.class,
                () -> bookService.addBook(null));
    }

    @Test
    @DisplayName("Should throw exception when adding duplicate book")
    void shouldThrowWhenAddingDuplicateBook() {
        Book book = new Book("Title", "Author", "123");
        bookService.addBook(book);

        assertThrows(IllegalArgumentException.class,
                () -> bookService.addBook(book));
    }

    // --- Get book tests ---

    @Test
    @DisplayName("Should return empty when book not found")
    void shouldReturnEmptyWhenBookNotFound() {
        Optional<Book> found = bookService.getBookById("non-existent");
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Should return all books")
    void shouldReturnAllBooks() {
        bookService.addBook(new Book("Book A", "Author A", "111"));
        bookService.addBook(new Book("Book B", "Author B", "222"));
        bookService.addBook(new Book("Book C", "Author C", "333"));

        List<Book> all = bookService.getAllBooks();
        assertEquals(3, all.size());
    }

    @Test
    @DisplayName("Should return empty list when no books exist")
    void shouldReturnEmptyListWhenNoBooksExist() {
        List<Book> all = bookService.getAllBooks();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }

    // --- Update book tests ---

    @Test
    @DisplayName("Should update book fields successfully")
    void shouldUpdateBookFieldsSuccessfully() {
        Book book = new Book("Old Title", "Old Author", "old-isbn");
        bookService.addBook(book);

        bookService.updateBook(book.getId(), "New Title", "New Author", "new-isbn");

        Book updated = bookService.getBookById(book.getId()).orElseThrow();
        assertEquals("New Title", updated.getTitle());
        assertEquals("New Author", updated.getAuthor());
        assertEquals("new-isbn", updated.getIsbn());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent book")
    void shouldThrowWhenUpdatingNonExistentBook() {
        assertThrows(IllegalArgumentException.class,
                () -> bookService.updateBook("fake-id", "T", "A", "I"));
    }

    // --- Delete book tests ---

    @Test
    @DisplayName("Should delete a book successfully")
    void shouldDeleteBookSuccessfully() {
        Book book = new Book("Title", "Author", "123");
        bookService.addBook(book);

        boolean deleted = bookService.deleteBook(book.getId());

        assertTrue(deleted);
        assertTrue(bookService.getBookById(book.getId()).isEmpty());
    }

    @Test
    @DisplayName("Should return false when deleting non-existent book")
    void shouldReturnFalseWhenDeletingNonExistentBook() {
        assertFalse(bookService.deleteBook("non-existent"));
    }
}
