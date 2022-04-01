package com.akvelon.dorodnikov.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Order model DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private int id;

    @Min(value = 0, message = "'customerId' field should not be less than 0")
    @NotNull(message = "'customerId' field is must not be null")
    private Integer customerId;
}