package com.library.model;

/**
 * Represents the current state of a book loan.
 */
// Action 1: Indentation reformatted from 4 to 2 spaces (Google Java Style)
public enum LoanStatus {
  /** The book is currently on loan. */
  ACTIVE,

  /** The book has been returned. */
  RETURNED
}
