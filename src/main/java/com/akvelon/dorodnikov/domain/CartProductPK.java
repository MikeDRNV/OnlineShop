package com.akvelon.dorodnikov.domain;

import com.akvelon.dorodnikov.domain.entites.CartProduct;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Composite primary key for {@link CartProduct}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CartProductPK implements Serializable {

    @Column(name = "product_id")
    private int productId;

    @Column(name = "cart_id")
    private int cartId;
}