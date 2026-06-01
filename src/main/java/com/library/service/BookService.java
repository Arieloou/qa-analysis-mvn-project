package com.library.service;

import com.library.model.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service responsible for CRUD operations on books.
 * Uses in-memory storage via a HashMap indexed by book ID.
 */
public class BookService {

    private final Map<String, Book> books;

    public BookService() {
        this.books = new HashMap<>();
    }

    /**
     * Adds a new book to the collection.
     *
     * @param book the book to add (must not be null)
     * @throws IllegalArgumentException if book is null or a book with the same ID
     *                                  already exists
     */
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book must not be null.");
        }
        if (books.containsKey(book.getId())) {
            throw new IllegalArgumentException("A book with this ID already exists.");
        }
        books.put(book.getId(), book);
    }

    /**
     * Retrieves a book by its unique ID.
     *
     * @param id the book ID
     * @return an Optional containing the book if found
     */
    public Optional<Book> getBookById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    /**
     * Returns all books in the system.
     *
     * @return a list of all books (never null)
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    /**
     * Updates an existing book's fields (title, author, ISBN).
     *
     * @param id     the ID of the book to update
     * @param title  the new title
     * @param author the new author
     * @param isbn   the new ISBN
     * @throws IllegalArgumentException if the book is not found
     */
    public void updateBook(String id, String title, String author, String isbn) {
        Book existing = books.get(id);
        if (existing == null) {
            throw new IllegalArgumentException("Book not found with ID: " + id);
        }
        existing.setTitle(title);
        existing.setAuthor(author);
        existing.setIsbn(isbn);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to delete
     * @return true if the book was found and removed, false otherwise
     */
    public boolean deleteBook(String id) {
        return books.remove(id) != null;
    }
}
