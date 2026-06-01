package com.library.model;

/**
 * Defines the available roles for users in the library system.
 */
// Action 1: Indentation reformatted from 4 to 2 spaces (Google Java Style)
public enum UserRole {
  /** Full access: can manage books, bookstores, users, and loans. */
  ADMIN,

  /** Limited access: can request and return book loans. */
  REQUESTER
}
