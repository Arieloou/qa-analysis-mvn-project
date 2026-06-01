package com.library.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a book loan in the library system.
 * Tracks which book was loaned to which user, the timeline, and status.
 */
public class Loan {

    private final String id;
    private final Book book;
    private final User user;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;

    /**
     * Creates a new active loan with a generated unique ID.
     *
     * @param book    the book being loaned (must not be null)
     * @param user    the user borrowing the book (must not be null)
     * @param dueDate the date by which the book must be returned (must be after today)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Loan(Book book, User user, LocalDate dueDate) {
        if (book == null) {
            throw new IllegalArgumentException("Book must not be null.");
        }
        if (user == null) {
            throw new IllegalArgumentException("User must not be null.");
        }
        if (dueDate == null || !dueDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Due date must be a future date.");
        }
        this.id = UUID.randomUUID().toString();
        this.book = book;
        this.user = user;
        this.loanDate = LocalDate.now();
        this.dueDate = dueDate;
        this.returnDate = null;
        this.status = LoanStatus.ACTIVE;
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    // --- Loan operations ---

    /**
     * Marks this loan as returned, setting the return date to today
     * and restoring the book's availability.
     *
     * @throws IllegalStateException if the loan was already returned
     */
    public void returnBook() {
        if (this.status == LoanStatus.RETURNED) {
            throw new IllegalStateException("This loan has already been returned.");
        }
        this.status = LoanStatus.RETURNED;
        this.returnDate = LocalDate.now();
        this.book.setAvailable(true);
    }

    /**
     * Checks whether this loan is overdue (past due date and still active).
     *
     * @return true if the loan is active and the due date has passed
     */
    public boolean isOverdue() {
        return this.status == LoanStatus.ACTIVE
                && LocalDate.now().isAfter(this.dueDate);
    }

    @Override
    public String toString() {
        return String.format(
                "Loan{id='%s', book='%s', user='%s', loanDate=%s, dueDate=%s, returnDate=%s, status=%s}",
                id, book.getTitle(), user.getName(), loanDate, dueDate, returnDate, status);
    }
}
