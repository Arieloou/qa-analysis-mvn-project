package com.library.model;

/**
 * Defines the available roles for users in the library system.
 */
public enum UserRole {
    /** Full access: can manage books, bookstores, users, and loans. */
    ADMIN,

    /** Limited access: can request and return book loans. */
    REQUESTER
}
