package com.library.service;

import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.LoanStatus;
import com.library.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service responsible for managing book loans and returns.
 * Coordinates between books and users to enforce loan business rules.
 */
public class LoanService {

  // In-memory store keyed by loan ID
  private final Map<String, Loan> loans;

  public LoanService() {
    // Action 1: Indentation reformatted from 4 to 2 spaces (Google Java Style)
    this.loans = new HashMap<>();
  }

  /**
   * Creates a new loan for a book and a user.
   * The book is marked as unavailable upon successful loan creation.
   *
   * @param book    the book to loan (must be available)
   * @param user    the user borrowing the book
   * @param dueDate the date by which the book must be returned
   * @return the created Loan
   * @throws IllegalArgumentException if book or user is null
   * @throws IllegalStateException    if the book is not available for loan
   */
  public Loan loanBook(Book book, User user, LocalDate dueDate) {
    if (book == null) {
      throw new IllegalArgumentException("Book must not be null.");
    }
    if (user == null) {
      throw new IllegalArgumentException("User must not be null.");
    }
    if (!book.isAvailable()) {
      throw new IllegalStateException(
          "Book is not available for loan: " + book.getTitle());
    }

    // Mark book as unavailable
    book.setAvailable(false);

    // Create and store the loan
    Loan loan = new Loan(book, user, dueDate);
    loans.put(loan.getId(), loan);
    return loan;
  }

  /**
   * Processes the return of a loaned book.
   * The loan is marked as returned and the book becomes available again.
   *
   * @param loanId the ID of the loan to return
   * @throws IllegalArgumentException if the loan is not found
   * @throws IllegalStateException    if the loan was already returned
   */
  public void returnBook(String loanId) {
    Loan loan = loans.get(loanId);
    if (loan == null) {
      throw new IllegalArgumentException(
          "Loan not found with ID: " + loanId);
    }
    loan.returnBook();
  }

  /**
   * Retrieves a loan by its unique ID.
   *
   * @param id the loan ID
   * @return an Optional containing the loan if found
   */
  public Optional<Loan> getLoanById(String id) {
    return Optional.ofNullable(loans.get(id));
  }

  /**
   * Returns all loans in the system.
   *
   * @return a list of all loans (never null)
   */
  public List<Loan> getAllLoans() {
    return new ArrayList<>(loans.values());
  }

  /**
   * Returns all active (non-returned) loans for a specific user.
   *
   * @param userId the user's ID
   * @return a list of active loans for the user
   */
  public List<Loan> getActiveLoansByUser(String userId) {
    return loans.values().stream()
        .filter(loan -> loan.getUser().getId().equals(userId))
        .filter(loan -> loan.getStatus() == LoanStatus.ACTIVE)
        .collect(Collectors.toList());
  }
}
