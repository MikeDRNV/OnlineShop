package com.akvelon.dorodnikov.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cart model DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private int id;

    @Min(value = 0, message = "'userId' field should not be less than 0")
    @NotNull(message = "'userId' field is must not be null")
    private Integer userId;

    @NotNull(message = "'isCompleted' field is must not be null")
    private Boolean isCompleted;
}