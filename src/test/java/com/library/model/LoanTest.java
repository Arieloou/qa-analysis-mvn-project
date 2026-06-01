package com.library.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Loan model.
 */
class LoanTest {

    // --- Helper method ---

    private Book createAvailableBook() {
        return new Book("Test Book", "Test Author", "ISBN-001");
    }

    private User createUser() {
        return new User("John Doe", "john@test.com", UserRole.REQUESTER);
    }

    // --- Construction tests ---

    @Test
    @DisplayName("Should create a valid loan with ACTIVE status")
    void shouldCreateValidLoan() {
        Book book = createAvailableBook();
        User user = createUser();
        LocalDate dueDate = LocalDate.now().plusDays(14);

        Loan loan = new Loan(book, user, dueDate);

        assertNotNull(loan.getId());
        assertEquals(book, loan.getBook());
        assertEquals(user, loan.getUser());
        assertEquals(LocalDate.now(), loan.getLoanDate());
        assertEquals(dueDate, loan.getDueDate());
        assertNull(loan.getReturnDate());
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when book is null")
    void shouldThrowWhenBookIsNull() {
        User user = createUser();
        LocalDate dueDate = LocalDate.now().plusDays(14);

        assertThrows(IllegalArgumentException.class,
                () -> new Loan(null, user, dueDate));
    }

    @Test
    @DisplayName("Should throw exception when user is null")
    void shouldThrowWhenUserIsNull() {
        Book book = createAvailableBook();
        LocalDate dueDate = LocalDate.now().plusDays(14);

        assertThrows(IllegalArgumentException.class,
                () -> new Loan(book, null, dueDate));
    }

    @Test
    @DisplayName("Should throw exception when due date is null")
    void shouldThrowWhenDueDateIsNull() {
        Book book = createAvailableBook();
        User user = createUser();

        assertThrows(IllegalArgumentException.class,
                () -> new Loan(book, user, null));
    }

    @Test
    @DisplayName("Should throw exception when due date is today")
    void shouldThrowWhenDueDateIsToday() {
        Book book = createAvailableBook();
        User user = createUser();

        assertThrows(IllegalArgumentException.class,
                () -> new Loan(book, user, LocalDate.now()));
    }

    @Test
    @DisplayName("Should throw exception when due date is in the past")
    void shouldThrowWhenDueDateIsInThePast() {
        Book book = createAvailableBook();
        User user = createUser();

        assertThrows(IllegalArgumentException.class,
                () -> new Loan(book, user, LocalDate.now().minusDays(1)));
    }

    // --- Return book tests ---

    @Test
    @DisplayName("Should return book and update status")
    void shouldReturnBookAndUpdateStatus() {
        Book book = createAvailableBook();
        User user = createUser();
        Loan loan = new Loan(book, user, LocalDate.now().plusDays(14));

        // Mark book as unavailable (simulating loan)
        book.setAvailable(false);

        loan.returnBook();

        assertEquals(LoanStatus.RETURNED, loan.getStatus());
        assertEquals(LocalDate.now(), loan.getReturnDate());
        assertTrue(book.isAvailable(), "Book should be available after return");
    }

    @Test
    @DisplayName("Should throw exception when returning already returned loan")
    void shouldThrowWhenReturningAlreadyReturnedLoan() {
        Book book = createAvailableBook();
        User user = createUser();
        Loan loan = new Loan(book, user, LocalDate.now().plusDays(14));

        loan.returnBook();

        assertThrows(IllegalStateException.class, loan::returnBook);
    }

    // --- Overdue tests ---

    @Test
    @DisplayName("Should not be overdue when due date is in the future")
    void shouldNotBeOverdueWhenDueDateIsInFuture() {
        Book book = createAvailableBook();
        User user = createUser();
        Loan loan = new Loan(book, user, LocalDate.now().plusDays(14));

        assertFalse(loan.isOverdue());
    }

    @Test
    @DisplayName("Should not be overdue when loan is returned")
    void shouldNotBeOverdueWhenLoanIsReturned() {
        Book book = createAvailableBook();
        User user = createUser();
        Loan loan = new Loan(book, user, LocalDate.now().plusDays(1));

        loan.returnBook();

        assertFalse(loan.isOverdue(), "Returned loans should not be overdue");
    }

    // --- toString test ---

    @Test
    @DisplayName("Should produce a readable string representation")
    void shouldProduceReadableToString() {
        Book book = createAvailableBook();
        User user = createUser();
        Loan loan = new Loan(book, user, LocalDate.now().plusDays(14));

        String result = loan.toString();

        assertTrue(result.contains("Test Book"));
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("ACTIVE"));
    }
}
