package com.akvelon.dorodnikov.controllers;

import com.akvelon.dorodnikov.ErrorResponseDTO;
import com.akvelon.dorodnikov.dto.ErrorDTO;
import com.akvelon.dorodnikov.utils.exceptions.NotEmptyCartDeletionException;
import com.akvelon.dorodnikov.utils.exceptions.NotExistUserException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationPageException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationSizeException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationSortException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Global exception handler.
 */
@RestController
@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    /**
     * Returns error message if there was an attempt to delete a non-empty cart.
     *
     * @param e Message of constraint violations reported during a validation.
     * @return ResponseEntity with HTTP status and error message.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotEmptyCartDeletionException.class})
    public ErrorResponseDTO handleNotEmptyCartDeletionException(NotEmptyCartDeletionException e) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.getErrorDtos()
                .add(new ErrorDTO("The cart can't be deleted because it's not empty. " + e.getCause().getMessage()));
        return error;
    }

    /**
     * Returns error message if there was an attempt to get a non-existent user.
     *
     * @param e Error message that contains the user ID.
     * @return ResponseEntity with HTTP status and error message.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotExistUserException.class})
    public ErrorResponseDTO handleNotExistUserException(NotExistUserException e) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.getErrorDtos().add(new ErrorDTO("User with ID " + e.getMessage() + " does not exist"));
        return error;
    }

    /**
     * Returns error message if there was an attempt to sort by an incorrect direction and to get a page and the page
     * size is less than one.
     *
     * @param e Error message that contains incorrect parameters in request.
     * @return ResponseEntity with HTTP status and error message.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({PaginationException.class})
    public ErrorResponseDTO handlePaginationException(PaginationException e) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.getErrorDtos().add(new ErrorDTO(e.getMessage()));
        return error;
    }

    /**
     * Returns an error message if an attempt was made to sort by an incorrect parameter.
     *
     * @param e Error message that contains incorrect parameter in request.
     * @return ResponseEntity with HTTP status and error message.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({PaginationSortException.class})
    public ErrorResponseDTO handlePaginationSortException(PaginationSortException e) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.getErrorDtos().add(new ErrorDTO("The '" + e.getMessage() + "' property was not found to sort the list"));
        return error;
    }

    /**
     * Returns error message if there was an attempt to get page whose number is greater than the total number of
     * pages.
     *
     * @param e Error message that contains incorrect parameters in request.
     * @return ResponseEntity with HTTP status and error message.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({PaginationPageException.class})
    public ErrorResponseDTO handlePaginationPageException(PaginationPageException e) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.getErrorDtos().add(new ErrorDTO("Page number '" + e.getMessage() + "' not found"));
        return error;
    }

    /**
     * Returns error message if there was an attempt to get a page size that is larger than the total number of
     * elements.
     *
     * @param e Error message that contains incorrect parameters in request.
     * @return ResponseEntity with HTTP status and error message.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({PaginationSizeException.class})
    public ErrorResponseDTO handlePaginationSizeException(PaginationSizeException e) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.getErrorDtos()
                .add(new ErrorDTO("Page size '" + e.getMessage() + "' is larger than the total number of elements"));
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ErrorResponseDTO handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.getErrorDtos().add(new ErrorDTO(e.getMessage()));
        return error;
    }

    /**
     * Returns list error messages.
     *
     * @param e Exception to be thrown when validation on an argument annotated with @Valid fails.
     * @return The list of error messages.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getErrorDtos().add(new ErrorDTO(fieldError.getDefaultMessage()));
        }
        return error;
    }
}