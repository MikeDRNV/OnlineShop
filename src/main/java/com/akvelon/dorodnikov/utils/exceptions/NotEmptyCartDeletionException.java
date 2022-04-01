package com.akvelon.dorodnikov.utils.exceptions;

import com.akvelon.dorodnikov.domain.entites.CartEntity;

import lombok.ToString;

/**
 * {@code NotEmptyCartDeletionException} occurs when trying to delete a non-empty {@link CartEntity}.
 */
@ToString
public class NotEmptyCartDeletionException extends RuntimeException {

    /**
     * Constructs a new {@code NotEmptyCartDeletionException} with the specified detail message and cause.
     *
     * @param errorMessage Detailed error message that led to the exception.
     * @param cause Cause why the exception occurred.
     */
    public NotEmptyCartDeletionException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}