package com.akvelon.dorodnikov.utils.exceptions;

/**
 * {@code PaginationPageException} occurs when trying to get a page whose number is greater than the total number of
 * pages.
 */
public class PaginationPageException extends PaginationException {

    /**
     * Constructs a new {@code PaginationPageException} with the specified detail message.
     *
     * @param errorMessage Detailed error message that led to the exception.
     */
    public PaginationPageException(String errorMessage) {
        super(errorMessage);
    }
}