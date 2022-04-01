package com.akvelon.dorodnikov.domain.repositories;

import com.akvelon.dorodnikov.domain.CartProductPK;
import com.akvelon.dorodnikov.domain.entites.CartProduct;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

/**
 * Provides CRUD methods for {@link CartProduct}
 */
@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, CartProductPK> {

    CartProduct findByCartIdAndProductId(int cartId, int productId);

    CartProduct findByCartId(int cartId);
}