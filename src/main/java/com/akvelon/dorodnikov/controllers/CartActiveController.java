package com.akvelon.dorodnikov.controllers;

import com.akvelon.dorodnikov.controllers.helpers.BaseController;
import com.akvelon.dorodnikov.domain.entites.ActiveCart;
import com.akvelon.dorodnikov.domain.entites.CartEntity;
import com.akvelon.dorodnikov.domain.services.CartService;
import com.akvelon.dorodnikov.dto.ActiveCartDTO;
import com.akvelon.dorodnikov.dto.CartDTO;
import com.akvelon.dorodnikov.dto.CartProductDTO;
import com.akvelon.dorodnikov.dto.ProductQuantityDTO;
import com.akvelon.dorodnikov.utils.MappingUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for "Cart" domain entity requests in "active" state.
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/api/v1/cart/active")
public class CartActiveController extends BaseController {

    private final CartService cartService;

    /**
     * Returns ResponseEntity with HTTP status and list of {@link CartProductDTO}-s.
     *
     * @param userId User ID.
     * @return ResponseEntity with HTTP status and {@link ActiveCartDTO}.
     */
    @GetMapping("/{userId}/products")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getListOfProductsInCart(@PathVariable int userId) {
        ActiveCart activeCart = cartService.getProductsInCart(userId);
        if (activeCart == null) {
            return messageWithStatus("The user with ID " + userId + " does not have active cart",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(MappingUtils.mapActiveCartToActiveCartDTO(activeCart), HttpStatus.OK);
    }

    /**
     * Adds product to active cart.
     *
     * @param userId User ID.
     * @param productQuantityDTO Contains product ID and quantity of this product.
     * @return ResponseEntity with HTTP status and {@link ActiveCartDTO}.
     */
    @PostMapping("/{userId}/changeProductQuantity")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity addProductToCart(@PathVariable int userId,
            @RequestBody @Valid ProductQuantityDTO productQuantityDTO) {
        ActiveCart activeCart = cartService.addProductToActive(userId, productQuantityDTO.getProductId(),
                productQuantityDTO.getQuantity());
        if (activeCart == null) {
            return messageWithStatus("Product with ID " + productQuantityDTO.getProductId() + " does not exist",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(MappingUtils.mapActiveCartToActiveCartDTO(activeCart), HttpStatus.CREATED);
    }

    /**
     * Converts cart to order.
     *
     * @param userId ID of the user whose shopping cart becomes an order.
     * @return ResponseEntity with HTTP status and {@link CartDTO}.
     */
    @PatchMapping("/{userId}/order")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity makeOrder(@PathVariable int userId) {
        CartEntity cartEntity = cartService.makeOrder(userId);
        if (cartEntity == null) {
            return messageWithStatus("The user with ID " + userId + " does not have active cart",
                    HttpStatus.BAD_REQUEST);
        } else if (cartEntity.getCartProductList().isEmpty()) {
            return messageWithStatus("The user with ID " + userId + " has an empty shopping cart",
                    HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(MappingUtils.mapToCartDTO(cartEntity), HttpStatus.OK);
        }
    }
}