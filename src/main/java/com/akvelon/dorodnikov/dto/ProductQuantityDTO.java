package com.akvelon.dorodnikov.dto;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Product ID with quantity of this product.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantityDTO {

    @Min(value = 0, message = "'productId' field should not be less than 0")
    private int productId;

    @Min(value = 0, message = "'quantity' field should not be less than 0")
    private int quantity;
}