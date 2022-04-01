package com.akvelon.dorodnikov.controllers.helpers;

import com.akvelon.dorodnikov.ErrorResponseDTO;
import com.akvelon.dorodnikov.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Base class for controller classes.
 */
public class BaseController {

    /**
     * Returns HTTP response with status code and containing a specific error message in the request body.
     *
     * @param message Error description.
     * @param httpStatus HTTP request status.
     * @return HTTP response.
     */
    public static ResponseEntity messageWithStatus(String message, HttpStatus httpStatus) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.getErrorDtos().add(new ErrorDTO(message));
        return new ResponseEntity<>(error, httpStatus);
    }
}