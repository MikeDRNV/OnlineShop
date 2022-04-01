package com.akvelon.dorodnikov.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Provides error message.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ErrorDTO {
    private String message;
}