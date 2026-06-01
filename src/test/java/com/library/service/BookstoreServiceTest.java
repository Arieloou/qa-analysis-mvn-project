package com.library.service;

import com.library.model.Book;
import com.library.model.Bookstore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the BookstoreService.
 * Verifies CRUD operations and cascade deletion behavior.
 */
class BookstoreServiceTest {

    private BookService bookService;
    private BookstoreService bookstoreService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        bookstoreService = new BookstoreService(bookService);
    }

    // --- Constructor tests ---

    @Test
    @DisplayName("Should throw exception when BookService dependency is null")
    void shouldThrowWhenBookServiceIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new BookstoreService(null));
    }

    // --- Add bookstore tests ---

    @Test
    @DisplayName("Should add a bookstore successfully")
    void shouldAddBookstoreSuccessfully() {
        Bookstore store = new Bookstore("Main Library", "100 Center St");
        bookstoreService.addBookstore(store);

        Optional<Bookstore> found = bookstoreService.getBookstoreById(store.getId());
        assertTrue(found.isPresent());
        assertEquals("Main Library", found.get().getName());
    }

    @Test
    @DisplayName("Should throw exception when adding null bookstore")
    void shouldThrowWhenAddingNullBookstore() {
        assertThrows(IllegalArgumentException.class,
                () -> bookstoreService.addBookstore(null));
    }

    @Test
    @DisplayName("Should throw exception when adding duplicate bookstore")
    void shouldThrowWhenAddingDuplicateBookstore() {
        Bookstore store = new Bookstore("Library", "Address");
        bookstoreService.addBookstore(store);

        assertThrows(IllegalArgumentException.class,
                () -> bookstoreService.addBookstore(store));
    }

    // --- Get bookstore tests ---

    @Test
    @DisplayName("Should return empty when bookstore not found")
    void shouldReturnEmptyWhenBookstoreNotFound() {
        assertTrue(bookstoreService.getBookstoreById("fake-id").isEmpty());
    }

    @Test
    @DisplayName("Should return all bookstores")
    void shouldReturnAllBookstores() {
        bookstoreService.addBookstore(new Bookstore("Store A", "Addr A"));
        bookstoreService.addBookstore(new Bookstore("Store B", "Addr B"));

        List<Bookstore> all = bookstoreService.getAllBookstores();
        assertEquals(2, all.size());
    }

    // --- Update bookstore tests ---

    @Test
    @DisplayName("Should update bookstore fields successfully")
    void shouldUpdateBookstoreFieldsSuccessfully() {
        Bookstore store = new Bookstore("Old Name", "Old Address");
        bookstoreService.addBookstore(store);

        bookstoreService.updateBookstore(store.getId(), "New Name", "New Address");

        Bookstore updated = bookstoreService.getBookstoreById(store.getId()).orElseThrow();
        assertEquals("New Name", updated.getName());
        assertEquals("New Address", updated.getAddress());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent bookstore")
    void shouldThrowWhenUpdatingNonExistentBookstore() {
        assertThrows(IllegalArgumentException.class,
                () -> bookstoreService.updateBookstore("fake-id", "N", "A"));
    }

    // --- Delete bookstore tests ---

    @Test
    @DisplayName("Should delete bookstore successfully")
    void shouldDeleteBookstoreSuccessfully() {
        Bookstore store = new Bookstore("Library", "Address");
        bookstoreService.addBookstore(store);

        boolean deleted = bookstoreService.deleteBookstore(store.getId());

        assertTrue(deleted);
        assertTrue(bookstoreService.getBookstoreById(store.getId()).isEmpty());
    }

    @Test
    @DisplayName("Should return false when deleting non-existent bookstore")
    void shouldReturnFalseWhenDeletingNonExistentBookstore() {
        assertFalse(bookstoreService.deleteBookstore("fake-id"));
    }

    // --- Cascade delete tests ---

    @Test
    @DisplayName("Should cascade delete books when bookstore is deleted")
    void shouldCascadeDeleteBooks() {
        Bookstore store = new Bookstore("Library", "Address");
        bookstoreService.addBookstore(store);

        // Add books to the bookstore (also registers them in BookService)
        Book book1 = new Book("Book A", "Author A", "111");
        Book book2 = new Book("Book B", "Author B", "222");
        bookstoreService.addBookToBookstore(store.getId(), book1);
        bookstoreService.addBookToBookstore(store.getId(), book2);

        // Verify books exist in global service
        assertEquals(2, bookService.getAllBooks().size());

        // Delete bookstore
        bookstoreService.deleteBookstore(store.getId());

        // Verify books were cascade-deleted from global service
        assertTrue(bookService.getAllBooks().isEmpty());
        assertTrue(bookService.getBookById(book1.getId()).isEmpty());
        assertTrue(bookService.getBookById(book2.getId()).isEmpty());
    }

    @Test
    @DisplayName("Should not affect other bookstores' books on cascade delete")
    void shouldNotAffectOtherBookstoresBooksOnCascadeDelete() {
        Bookstore store1 = new Bookstore("Library A", "Addr A");
        Bookstore store2 = new Bookstore("Library B", "Addr B");
        bookstoreService.addBookstore(store1);
        bookstoreService.addBookstore(store2);

        Book bookA = new Book("Book A", "Author A", "111");
        Book bookB = new Book("Book B", "Author B", "222");
        bookstoreService.addBookToBookstore(store1.getId(), bookA);
        bookstoreService.addBookToBookstore(store2.getId(), bookB);

        // Delete only store1
        bookstoreService.deleteBookstore(store1.getId());

        // bookA should be gone, bookB should remain
        assertTrue(bookService.getBookById(bookA.getId()).isEmpty());
        assertTrue(bookService.getBookById(bookB.getId()).isPresent());
    }

    // --- Add book to bookstore tests ---

    @Test
    @DisplayName("Should add book to bookstore and global service")
    void shouldAddBookToBookstoreAndGlobalService() {
        Bookstore store = new Bookstore("Library", "Address");
        bookstoreService.addBookstore(store);

        Book book = new Book("New Book", "New Author", "999");
        bookstoreService.addBookToBookstore(store.getId(), book);

        // Verify book exists in both the bookstore and global service
        assertEquals(1, store.getBooks().size());
        assertTrue(bookService.getBookById(book.getId()).isPresent());
    }

    @Test
    @DisplayName("Should throw exception when adding book to non-existent bookstore")
    void shouldThrowWhenAddingBookToNonExistentBookstore() {
        Book book = new Book("Book", "Author", "123");
        assertThrows(IllegalArgumentException.class,
                () -> bookstoreService.addBookToBookstore("fake-id", book));
    }
}
