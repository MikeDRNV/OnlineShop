package com.akvelon.dorodnikov.utils.exceptions;

/**
 * {@code PaginationSortException} occurs when trying to sort by a non-existent parameter.
 */
public class PaginationSortException extends PaginationException {

    /**
     * Constructs a new {@code PaginationSortException} with the specified detail message.
     *
     * @param errorMessage Detailed error message that led to the exception.
     */
    public PaginationSortException(String errorMessage) {
        super(errorMessage);
    }
}