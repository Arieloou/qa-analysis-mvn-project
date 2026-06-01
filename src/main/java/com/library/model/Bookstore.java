package com.library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a bookstore (library branch) that holds a collection of books.
 * Supports adding, removing, and searching books within its inventory.
 */
public class Bookstore {

    private final String id;
    private String name;
    private String address;
    private final List<Book> books;

    /**
     * Creates a new bookstore with a generated unique ID.
     *
     * @param name    the bookstore name (must not be null or blank)
     * @param address the bookstore address (must not be null or blank)
     * @throws IllegalArgumentException if any required field is null or blank
     */
    public Bookstore(String name, String address) {
        validateNotBlank(name, "Name");
        validateNotBlank(address, "Address");
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.books = new ArrayList<>();
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    /**
     * Returns an unmodifiable view of the books in this bookstore.
     */
    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    // --- Setters ---

    public void setName(String name) {
        validateNotBlank(name, "Name");
        this.name = name;
    }

    public void setAddress(String address) {
        validateNotBlank(address, "Address");
        this.address = address;
    }

    // --- Book collection operations ---

    /**
     * Adds a book to this bookstore's collection.
     *
     * @param book the book to add (must not be null)
     * @throws IllegalArgumentException if book is null
     */
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book must not be null.");
        }
        books.add(book);
    }

    /**
     * Removes a book from this bookstore by its ID.
     *
     * @param bookId the ID of the book to remove
     * @return true if the book was found and removed, false otherwise
     */
    public boolean removeBook(String bookId) {
        return books.removeIf(book -> book.getId().equals(bookId));
    }

    /**
     * Finds a book in this bookstore by its ID.
     *
     * @param bookId the ID to search for
     * @return an Optional containing the book if found
     */
    public Optional<Book> findBookById(String bookId) {
        return books.stream()
                .filter(book -> book.getId().equals(bookId))
                .findFirst();
    }

    // --- Validation ---

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be null or blank.");
        }
    }

    @Override
    public String toString() {
        return String.format("Bookstore{id='%s', name='%s', address='%s', bookCount=%d}",
                id, name, address, books.size());
    }
}
