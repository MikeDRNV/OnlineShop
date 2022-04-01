package com.akvelon.dorodnikov.domain.entites;

import com.akvelon.dorodnikov.domain.CartProductPK;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Cart-product model entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_product")
public class CartProduct {

    @EmbeddedId
    private CartProductPK id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id", updatable = false)
    private ProductEntity product;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @MapsId("cartId")
    @JoinColumn(name = "cart_id", foreignKey=@ForeignKey(name="cart_to_cart_products_relation"), updatable = false)
    private CartEntity cart;

    private int quantity;

    private BigDecimal price;
}