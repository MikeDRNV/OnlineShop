package com.akvelon.dorodnikov.utils.exceptions;

import com.akvelon.dorodnikov.domain.entites.UserEntity;

import lombok.ToString;

/**
 * {code NotExistUserException} occurs when trying to get not non-existent {@link UserEntity}.
 */
@ToString
public class NotExistUserException extends RuntimeException {

    /**
     * Constructs a new {@code NotExistUserException} with the specified detail message.
     *
     * @param errorMessage Detailed error message that led to the exception.
     */
    public NotExistUserException(String errorMessage) {
        super(errorMessage);
    }
}