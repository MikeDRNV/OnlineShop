package com.akvelon.dorodnikov.utils.exceptions;

/**
 * {@code PaginationSizeException} occurs when trying to get a page size that is larger than the total number of
 * elements.
 */
public class PaginationSizeException extends PaginationException {

    /**
     * Constructs a new {@code PaginationSizeException} with the specified detail message.
     *
     * @param errorMessage Detailed error message that led to the exception.
     */
    public PaginationSizeException(String errorMessage) {
        super(errorMessage);
    }
}