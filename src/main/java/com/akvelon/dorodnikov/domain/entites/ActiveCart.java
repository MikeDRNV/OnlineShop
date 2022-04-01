package com.akvelon.dorodnikov.domain.entites;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Active cart model entity.
 */
@Data
@AllArgsConstructor
public class ActiveCart {

    @Min(value = 0, message = "'cartId' field should not be less than 0")
    @NotNull(message = "'cartId' field is must not be null")
    private Integer cartId;

    @Min(value = 0, message = "'userId' field should not be less than 0")
    @NotNull(message = "'userId' field is must not be null")
    private Integer userId;

    @NotNull(message = "'cartItems' field is must not be null")
    List<CartItem> cartItems;
}