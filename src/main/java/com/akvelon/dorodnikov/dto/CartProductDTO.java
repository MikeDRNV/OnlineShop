package com.akvelon.dorodnikov.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cart-product model DTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDTO {

    @NotNull
    private ProductDTO productDTO;

    @Min(value = 0, message = "'price' field should not be less than 0")
    private int quantity;

    @Min(value = 1, message = "'price' field should not be less than 1")
    private BigDecimal price;
}