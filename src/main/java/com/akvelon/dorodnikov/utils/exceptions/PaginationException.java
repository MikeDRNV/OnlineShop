package com.akvelon.dorodnikov.utils.exceptions;

/**
 * {code PaginationException} occurs when trying to sort by an incorrect direction and when trying to get a page and
 * the page size is less than one.
 */
public class PaginationException extends RuntimeException {

    /**
     * Constructs a new {@code PaginationException} with the specified detail message.
     *
     * @param errorMessage Detailed error message that led to the exception.
     */
    public PaginationException(String errorMessage) {
        super(errorMessage);
    }
}