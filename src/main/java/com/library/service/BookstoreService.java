package com.library.service;

import com.library.model.Book;
import com.library.model.Bookstore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service responsible for CRUD operations on bookstores.
 * Depends on BookService to perform cascade deletion of associated books.
 */
public class BookstoreService {

  // In-memory store keyed by bookstore ID
  private final Map<String, Bookstore> bookstores;
  // Dependency for cascade book deletion
  private final BookService bookService;

  /**
   * Creates a BookstoreService with a dependency on BookService.
   * This dependency enables cascade deletion of books when a bookstore is
   * removed.
   *
   * @param bookService the book service for managing global book references
   */
  public BookstoreService(BookService bookService) {
    // Action 1: Indentation reformatted from 4 to 2 spaces (Google Java Style)
    if (bookService == null) {
      throw new IllegalArgumentException(
          "BookService must not be null.");
    }
    this.bookstores = new HashMap<>();
    this.bookService = bookService;
  }

  /**
   * Adds a new bookstore to the system.
   *
   * @param bookstore the bookstore to add (must not be null)
   * @throws IllegalArgumentException if bookstore is null or already exists
   */
  public void addBookstore(Bookstore bookstore) {
    if (bookstore == null) {
      throw new IllegalArgumentException(
          "Bookstore must not be null.");
    }
    if (bookstores.containsKey(bookstore.getId())) {
      throw new IllegalArgumentException(
          "A bookstore with this ID already exists.");
    }
    bookstores.put(bookstore.getId(), bookstore);
  }

  /**
   * Retrieves a bookstore by its unique ID.
   *
   * @param id the bookstore ID
   * @return an Optional containing the bookstore if found
   */
  public Optional<Bookstore> getBookstoreById(String id) {
    return Optional.ofNullable(bookstores.get(id));
  }

  /**
   * Returns all bookstores in the system.
   *
   * @return a list of all bookstores (never null)
   */
  public List<Bookstore> getAllBookstores() {
    return new ArrayList<>(bookstores.values());
  }

  /**
   * Updates an existing bookstore's fields (name, address).
   *
   * @param id      the ID of the bookstore to update
   * @param name    the new name
   * @param address the new address
   * @throws IllegalArgumentException if the bookstore is not found
   */
  public void updateBookstore(String id, String name, String address) {
    Bookstore existing = bookstores.get(id);
    if (existing == null) {
      throw new IllegalArgumentException(
          "Bookstore not found with ID: " + id);
    }
    existing.setName(name);
    existing.setAddress(address);
  }

  /**
   * Deletes a bookstore and cascade-deletes all books associated with it.
   * Each book in the bookstore is also removed from the global BookService.
   *
   * @param id the ID of the bookstore to delete
   * @return true if the bookstore was found and removed, false otherwise
   */
  public boolean deleteBookstore(String id) {
    Bookstore bookstore = bookstores.get(id);
    if (bookstore == null) {
      return false;
    }

    // Cascade delete: remove all books from the global book service
    for (Book book : bookstore.getBooks()) {
      bookService.deleteBook(book.getId());
    }

    bookstores.remove(id);
    return true;
  }

  /**
   * Adds a book to both a bookstore and the global book service.
   *
   * @param bookstoreId the ID of the target bookstore
   * @param book        the book to add
   * @throws IllegalArgumentException if the bookstore is not found or book is
   *                                  null
   */
  public void addBookToBookstore(String bookstoreId, Book book) {
    Bookstore bookstore = bookstores.get(bookstoreId);
    if (bookstore == null) {
      throw new IllegalArgumentException(
          "Bookstore not found with ID: " + bookstoreId);
    }
    bookstore.addBook(book);
    bookService.addBook(book);
  }
}
