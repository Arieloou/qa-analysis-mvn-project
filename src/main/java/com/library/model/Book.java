package com.library.model;

import java.util.UUID;

/**
 * Represents a book in the library system.
 * Each book has a unique ID, metadata, and an availability flag for loans.
 */
public class Book {

    private final String id;
    private String title;
    private String author;
    private String isbn;
    private boolean available;

    /**
     * Creates a new book with a generated unique ID.
     *
     * @param title  the book title (must not be null or blank)
     * @param author the book author (must not be null or blank)
     * @param isbn   the ISBN code (must not be null or blank)
     * @throws IllegalArgumentException if any required field is null or blank
     */
    public Book(String title, String author, String isbn) {
        validateFields(title, author, isbn);
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = true;
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isAvailable() {
        return available;
    }

    // --- Setters ---

    public void setTitle(String title) {
        validateNotBlank(title, "Title");
        this.title = title;
    }

    public void setAuthor(String author) {
        validateNotBlank(author, "Author");
        this.author = author;
    }

    public void setIsbn(String isbn) {
        validateNotBlank(isbn, "ISBN");
        this.isbn = isbn;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // --- Validation helpers ---

    /**
     * Validates that all required fields are non-null and non-blank.
     */
    private void validateFields(String title, String author, String isbn) {
        validateNotBlank(title, "Title");
        validateNotBlank(author, "Author");
        validateNotBlank(isbn, "ISBN");
    }

    /**
     * Validates that a single field is not null or blank.
     *
     * @param value     the value to check
     * @param fieldName the name of the field (for the error message)
     * @throws IllegalArgumentException if value is null or blank
     */
    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be null or blank.");
        }
    }

    @Override
    public String toString() {
        return String.format("Book{id='%s', title='%s', author='%s', isbn='%s', available=%s}",
                id, title, author, isbn, available);
    }
}
