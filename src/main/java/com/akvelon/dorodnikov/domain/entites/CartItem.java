package com.akvelon.dorodnikov.domain.entites;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Cart item model entity.
 */
@Data
@AllArgsConstructor
public class CartItem {

    @Min(value = 0, message = "'productId' field should not be less than 0")
    @NotNull(message = "'productId' field is must not be null")
    private Integer productId;

    @NotEmpty(message = "'name' field is empty")
    private String name;

    @NotEmpty(message = "'description' field is empty")
    private String description;

    @NotEmpty(message = "'imageUrl' field is empty")
    private String imageUrl;

    @Min(value = 0, message = "'quantity' field should not be less than 0")
    @NotNull(message = "'quantity' field is must not be null")
    private Integer quantity;

    @Min(value = 1, message = "'priceTotal' field should not be less than 1")
    @NotNull(message = "'priceTotal' field is must not be null")
    private BigDecimal priceTotal;
}