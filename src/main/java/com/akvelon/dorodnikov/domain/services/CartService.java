package com.akvelon.dorodnikov.domain.services;

import com.akvelon.dorodnikov.domain.entites.ActiveCart;
import com.akvelon.dorodnikov.domain.entites.CartEntity;
import com.akvelon.dorodnikov.domain.entites.CartItem;
import com.akvelon.dorodnikov.utils.exceptions.NotEmptyCartDeletionException;

import org.springframework.stereotype.Service;

/**
 * Defines the behavior for {@link CartEntity}.
 */
@Service
public interface CartService {

    /**
     * Returns {@link CartEntity}.
     *
     * @param id ID of CartEntity to be received.
     * @return CartEntity by ID.
     */
    CartEntity get(int id);

    /**
     * Creates new {@link CartEntity}.
     *
     * @param cartEntity CartEntity to be added.
     * @return CartEntity being saved in database.
     */
    CartEntity create(CartEntity cartEntity);

    /**
     * Updates {@link CartEntity}.
     *
     * @param id ID of CartEntity to be updated.
     * @param cartEntity Object containing the data to update.
     * @return CartEntity being updated in database.
     */
    CartEntity update(int id, CartEntity cartEntity);


    /**
     * Returns {@link ActiveCart} object that contains list of {@link CartItem}-s.
     *
     * @param userId User ID.
     * @return {@link ActiveCart} object that contains list of {@link CartItem}-s.
     */
    ActiveCart getProductsInCart(int userId);

    /**
     * Add product to active cart.
     * If the user does not have an active shopping cart, a new shopping cart will be created and this product will
     * be added to it with quantity 1.
     * If the user has an active shopping cart and it does not contain the product with this ID,
     * the product will be added to the cart with quantity 1.
     * If the product is contained in the cart, the quantity of the product will be changed to the quantity passed in
     * the parameters. If the quantity of the product is 0, it will be removed from the cart.
     *
     * @param userId User ID.
     * @param productId Product ID.
     * @param quantity Quantity of product.
     * @return {@link ActiveCart} object that contains list of {@link CartItem}-s.
     */
    ActiveCart addProductToActive(int userId, int productId, int quantity);


    /**
     * Converts shopping cart to order.
     *
     * @param userId ID of the user whose shopping cart becomes an order.
     * @return {@link CartEntity} state in database or {@code null} if the cart was not found.
     */
    CartEntity makeOrder(int userId);

    /**
     * Deletes only empty {@link CartEntity} otherwise returns error.
     *
     * @param id ID of CartEntity to be deleted.
     * @return {@code False} if ID do not exist, else return {@code true}.
     * @throws NotEmptyCartDeletionException when trying to delete a non-empty cart.
     */
    boolean delete(int id) throws NotEmptyCartDeletionException;
}