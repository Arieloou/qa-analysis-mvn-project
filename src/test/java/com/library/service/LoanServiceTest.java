package com.library.service;

import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.LoanStatus;
import com.library.model.User;
import com.library.model.UserRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the LoanService.
 */
class LoanServiceTest {

    private LoanService loanService;
    private Book book;
    private User user;

    @BeforeEach
    void setUp() {
        loanService = new LoanService();
        book = new Book("Test Book", "Test Author", "ISBN-001");
        user = new User("John Doe", "john@test.com", UserRole.REQUESTER);
    }

    // --- Loan creation tests ---

    @Test
    @DisplayName("Should create a loan successfully")
    void shouldCreateLoanSuccessfully() {
        LocalDate dueDate = LocalDate.now().plusDays(14);
        Loan loan = loanService.loanBook(book, user, dueDate);

        assertNotNull(loan);
        assertNotNull(loan.getId());
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
        assertFalse(book.isAvailable(), "Book should be unavailable after loan");
    }

    @Test
    @DisplayName("Should throw exception when loaning null book")
    void shouldThrowWhenLoaningNullBook() {
        assertThrows(IllegalArgumentException.class,
                () -> loanService.loanBook(null, user, LocalDate.now().plusDays(7)));
    }

    @Test
    @DisplayName("Should throw exception when loaning to null user")
    void shouldThrowWhenLoaningToNullUser() {
        assertThrows(IllegalArgumentException.class,
                () -> loanService.loanBook(book, null, LocalDate.now().plusDays(7)));
    }

    @Test
    @DisplayName("Should throw exception when loaning unavailable book")
    void shouldThrowWhenLoaningUnavailableBook() {
        book.setAvailable(false);

        assertThrows(IllegalStateException.class,
                () -> loanService.loanBook(book, user, LocalDate.now().plusDays(7)));
    }

    @Test
    @DisplayName("Should mark book as unavailable after loan")
    void shouldMarkBookAsUnavailableAfterLoan() {
        assertTrue(book.isAvailable());

        loanService.loanBook(book, user, LocalDate.now().plusDays(14));

        assertFalse(book.isAvailable());
    }

    // --- Return book tests ---

    @Test
    @DisplayName("Should return a loaned book successfully")
    void shouldReturnLoanedBookSuccessfully() {
        Loan loan = loanService.loanBook(book, user, LocalDate.now().plusDays(14));

        loanService.returnBook(loan.getId());

        assertEquals(LoanStatus.RETURNED, loan.getStatus());
        assertTrue(book.isAvailable(), "Book should be available after return");
    }

    @Test
    @DisplayName("Should throw exception when returning non-existent loan")
    void shouldThrowWhenReturningNonExistentLoan() {
        assertThrows(IllegalArgumentException.class,
                () -> loanService.returnBook("non-existent-id"));
    }

    @Test
    @DisplayName("Should throw exception when returning already returned loan")
    void shouldThrowWhenReturningAlreadyReturnedLoan() {
        Loan loan = loanService.loanBook(book, user, LocalDate.now().plusDays(14));
        loanService.returnBook(loan.getId());

        assertThrows(IllegalStateException.class,
                () -> loanService.returnBook(loan.getId()));
    }

    // --- Query tests ---

    @Test
    @DisplayName("Should retrieve a loan by ID")
    void shouldRetrieveLoanById() {
        Loan loan = loanService.loanBook(book, user, LocalDate.now().plusDays(14));

        assertTrue(loanService.getLoanById(loan.getId()).isPresent());
    }

    @Test
    @DisplayName("Should return empty for non-existent loan ID")
    void shouldReturnEmptyForNonExistentLoanId() {
        assertTrue(loanService.getLoanById("fake-id").isEmpty());
    }

    @Test
    @DisplayName("Should return all loans")
    void shouldReturnAllLoans() {
        Book book2 = new Book("Book 2", "Author 2", "ISBN-002");
        loanService.loanBook(book, user, LocalDate.now().plusDays(7));
        loanService.loanBook(book2, user, LocalDate.now().plusDays(7));

        List<Loan> all = loanService.getAllLoans();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("Should return active loans by user")
    void shouldReturnActiveLoansByUser() {
        Book book2 = new Book("Book 2", "Author 2", "ISBN-002");

        Loan loan1 = loanService.loanBook(book, user, LocalDate.now().plusDays(14));
        loanService.loanBook(book2, user, LocalDate.now().plusDays(14));

        // Return one loan
        loanService.returnBook(loan1.getId());

        List<Loan> activeLoans = loanService.getActiveLoansByUser(user.getId());
        assertEquals(1, activeLoans.size());
        assertEquals(LoanStatus.ACTIVE, activeLoans.get(0).getStatus());
    }

    @Test
    @DisplayName("Should return empty list when user has no active loans")
    void shouldReturnEmptyListWhenUserHasNoActiveLoans() {
        List<Loan> activeLoans = loanService.getActiveLoansByUser(user.getId());
        assertTrue(activeLoans.isEmpty());
    }

    @Test
    @DisplayName("Should not return loans from different users")
    void shouldNotReturnLoansFromDifferentUsers() {
        User otherUser = new User("Jane", "jane@test.com", UserRole.REQUESTER);

        loanService.loanBook(book, user, LocalDate.now().plusDays(14));

        List<Loan> otherLoans = loanService.getActiveLoansByUser(otherUser.getId());
        assertTrue(otherLoans.isEmpty());
    }
}
