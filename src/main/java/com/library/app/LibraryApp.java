package com.library.app;

import com.library.model.Book;
import com.library.model.Bookstore;
import com.library.model.Loan;
import com.library.model.User;
import com.library.model.UserRole;
import com.library.service.BookService;
import com.library.service.BookstoreService;
import com.library.service.LoanService;
import com.library.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Interactive CLI application for the library management system.
 * Provides menu-driven access to all CRUD and loan operations.
 */
public class LibraryApp {

    private final BookService bookService;
    private final BookstoreService bookstoreService;
    private final UserService userService;
    private final LoanService loanService;
    private final Scanner scanner;

    /**
     * Initializes the application with all required services.
     */
    public LibraryApp() {
        this.bookService = new BookService();
        this.bookstoreService = new BookstoreService(bookService);
        this.userService = new UserService();
        this.loanService = new LoanService();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Entry point of the application.
     */
    public static void main(String[] args) {
        LibraryApp app = new LibraryApp();
        app.run();
    }

    /**
     * Main loop that displays the menu and processes user choices.
     */
    public void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt();
            switch (choice) {
                case 1 -> manageBooks();
                case 2 -> manageBookstores();
                case 3 -> manageUsers();
                case 4 -> manageLoans();
                case 0 -> {
                    System.out.println("Exiting application. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    // --- Main menu ---

    private void printMainMenu() {
        System.out.println("\n========================================");
        System.out.println("       LIBRARY MANAGEMENT SYSTEM        ");
        System.out.println("========================================");
        System.out.println("  1. Book Management");
        System.out.println("  2. Bookstore Management");
        System.out.println("  3. User Management");
        System.out.println("  4. Loan Management");
        System.out.println("  0. Exit");
        System.out.println("========================================");
        System.out.print("Select an option: ");
    }

    // --- Book Management ---

    private void manageBooks() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Book Management ---");
            System.out.println("  1. Add Book");
            System.out.println("  2. List All Books");
            System.out.println("  3. Update Book");
            System.out.println("  4. Delete Book");
            System.out.println("  0. Back");
            System.out.print("Select an option: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> addBook();
                case 2 -> listBooks();
                case 3 -> updateBook();
                case 4 -> deleteBook();
                case 0 -> inMenu = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addBook() {
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        try {
            Book book = new Book(title, author, isbn);
            bookService.addBook(book);
            System.out.println("Book added successfully. ID: " + book.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books registered.");
            return;
        }
        System.out.println("\n--- All Books ---");
        for (Book book : books) {
            System.out.println("  " + book);
        }
    }

    private void updateBook() {
        System.out.print("Book ID to update: ");
        String id = scanner.nextLine();
        System.out.print("New title: ");
        String title = scanner.nextLine();
        System.out.print("New author: ");
        String author = scanner.nextLine();
        System.out.print("New ISBN: ");
        String isbn = scanner.nextLine();

        try {
            bookService.updateBook(id, title, author, isbn);
            System.out.println("Book updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteBook() {
        System.out.print("Book ID to delete: ");
        String id = scanner.nextLine();
        if (bookService.deleteBook(id)) {
            System.out.println("Book deleted successfully.");
        } else {
            System.out.println("Book not found.");
        }
    }

    // --- Bookstore Management ---

    private void manageBookstores() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Bookstore Management ---");
            System.out.println("  1. Add Bookstore");
            System.out.println("  2. List All Bookstores");
            System.out.println("  3. Update Bookstore");
            System.out.println("  4. Delete Bookstore (Cascade)");
            System.out.println("  5. Add Book to Bookstore");
            System.out.println("  0. Back");
            System.out.print("Select an option: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> addBookstore();
                case 2 -> listBookstores();
                case 3 -> updateBookstore();
                case 4 -> deleteBookstore();
                case 5 -> addBookToBookstore();
                case 0 -> inMenu = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addBookstore() {
        System.out.print("Bookstore name: ");
        String name = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();

        try {
            Bookstore bookstore = new Bookstore(name, address);
            bookstoreService.addBookstore(bookstore);
            System.out.println("Bookstore added successfully. ID: " + bookstore.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listBookstores() {
        List<Bookstore> stores = bookstoreService.getAllBookstores();
        if (stores.isEmpty()) {
            System.out.println("No bookstores registered.");
            return;
        }
        System.out.println("\n--- All Bookstores ---");
        for (Bookstore store : stores) {
            System.out.println("  " + store);
            for (Book book : store.getBooks()) {
                System.out.println("    -> " + book);
            }
        }
    }

    private void updateBookstore() {
        System.out.print("Bookstore ID to update: ");
        String id = scanner.nextLine();
        System.out.print("New name: ");
        String name = scanner.nextLine();
        System.out.print("New address: ");
        String address = scanner.nextLine();

        try {
            bookstoreService.updateBookstore(id, name, address);
            System.out.println("Bookstore updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteBookstore() {
        System.out.print("Bookstore ID to delete: ");
        String id = scanner.nextLine();
        if (bookstoreService.deleteBookstore(id)) {
            System.out.println("Bookstore and associated books deleted successfully.");
        } else {
            System.out.println("Bookstore not found.");
        }
    }

    private void addBookToBookstore() {
        System.out.print("Bookstore ID: ");
        String storeId = scanner.nextLine();
        System.out.print("Book title: ");
        String title = scanner.nextLine();
        System.out.print("Book author: ");
        String author = scanner.nextLine();
        System.out.print("Book ISBN: ");
        String isbn = scanner.nextLine();

        try {
            Book book = new Book(title, author, isbn);
            bookstoreService.addBookToBookstore(storeId, book);
            System.out.println("Book added to bookstore. Book ID: " + book.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // --- User Management ---

    private void manageUsers() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- User Management ---");
            System.out.println("  1. Add User");
            System.out.println("  2. List All Users");
            System.out.println("  3. Update User");
            System.out.println("  4. Delete User");
            System.out.println("  0. Back");
            System.out.print("Select an option: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> addUser();
                case 2 -> listUsers();
                case 3 -> updateUser();
                case 4 -> deleteUser();
                case 0 -> inMenu = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addUser() {
        System.out.print("User name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Role (1=ADMIN, 2=REQUESTER): ");
        int roleChoice = readInt();

        UserRole role = (roleChoice == 1) ? UserRole.ADMIN : UserRole.REQUESTER;

        try {
            User user = new User(name, email, role);
            userService.addUser(user);
            System.out.println("User added successfully. ID: " + user.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users registered.");
            return;
        }
        System.out.println("\n--- All Users ---");
        for (User user : users) {
            System.out.println("  " + user);
        }
    }

    private void updateUser() {
        System.out.print("User ID to update: ");
        String id = scanner.nextLine();
        System.out.print("New name: ");
        String name = scanner.nextLine();
        System.out.print("New email: ");
        String email = scanner.nextLine();

        try {
            userService.updateUser(id, name, email);
            System.out.println("User updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteUser() {
        System.out.print("User ID to delete: ");
        String id = scanner.nextLine();
        if (userService.deleteUser(id)) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    // --- Loan Management ---

    private void manageLoans() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Loan Management ---");
            System.out.println("  1. Loan a Book");
            System.out.println("  2. Return a Book");
            System.out.println("  3. List All Loans");
            System.out.println("  4. List Active Loans by User");
            System.out.println("  0. Back");
            System.out.print("Select an option: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> loanBook();
                case 2 -> returnBook();
                case 3 -> listAllLoans();
                case 4 -> listActiveLoansByUser();
                case 0 -> inMenu = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void loanBook() {
        System.out.print("Book ID: ");
        String bookId = scanner.nextLine();
        System.out.print("User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Due date (days from now): ");
        int days = readInt();

        Optional<Book> bookOpt = bookService.getBookById(bookId);
        Optional<User> userOpt = userService.getUserById(userId);

        if (bookOpt.isEmpty()) {
            System.out.println("Book not found.");
            return;
        }
        if (userOpt.isEmpty()) {
            System.out.println("User not found.");
            return;
        }

        try {
            LocalDate dueDate = LocalDate.now().plusDays(days);
            Loan loan = loanService.loanBook(bookOpt.get(), userOpt.get(), dueDate);
            System.out.println("Loan created successfully. Loan ID: " + loan.getId());
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void returnBook() {
        System.out.print("Loan ID: ");
        String loanId = scanner.nextLine();

        try {
            loanService.returnBook(loanId);
            System.out.println("Book returned successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listAllLoans() {
        List<Loan> allLoans = loanService.getAllLoans();
        if (allLoans.isEmpty()) {
            System.out.println("No loans registered.");
            return;
        }
        System.out.println("\n--- All Loans ---");
        for (Loan loan : allLoans) {
            System.out.println("  " + loan);
        }
    }

    private void listActiveLoansByUser() {
        System.out.print("User ID: ");
        String userId = scanner.nextLine();
        List<Loan> activeLoans = loanService.getActiveLoansByUser(userId);
        if (activeLoans.isEmpty()) {
            System.out.println("No active loans for this user.");
            return;
        }
        System.out.println("\n--- Active Loans ---");
        for (Loan loan : activeLoans) {
            System.out.println("  " + loan);
        }
    }

    // --- Input helpers ---

    /**
     * Reads an integer from stdin, consuming the trailing newline.
     *
     * @return the parsed integer, or -1 if input is invalid
     */
    private int readInt() {
        try {
            String line = scanner.nextLine();
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
