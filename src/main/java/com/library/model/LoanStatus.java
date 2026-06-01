package com.library.model;

/**
 * Represents the current state of a book loan.
 */
public enum LoanStatus {
    /** The book is currently on loan. */
    ACTIVE,

    /** The book has been returned. */
    RETURNED
}
