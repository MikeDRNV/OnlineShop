package com.akvelon.dorodnikov;

import com.akvelon.dorodnikov.dto.ErrorDTO;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides list of error messages.
 */
@Getter
public class ErrorResponseDTO {
    private List<ErrorDTO> errorDtos = new ArrayList<>();
}