package com.akvelon.dorodnikov.controllers;

import com.akvelon.dorodnikov.controllers.helpers.BaseController;
import com.akvelon.dorodnikov.domain.entites.CartEntity;
import com.akvelon.dorodnikov.domain.services.CartService;
import com.akvelon.dorodnikov.dto.CartDTO;
import com.akvelon.dorodnikov.utils.mappers.CartMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for "Cart" entity CRUD operations.
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/api/v1/cart")
public class CartController extends BaseController {

    private final CartService cartService;

    private final CartMapper cartMapper;

    /**
     * Returns ResponseEntity with HTTP status and {@link CartDTO}.
     *
     * @param id ID of CartDTO to be received.
     * @return ResponseEntity with HTTP status and CartDTO by ID.
     */
    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable int id) {
        CartEntity cartEntity = cartService.get(id);
        if (cartEntity != null) {
            return new ResponseEntity(cartMapper.toCartDTO(cartEntity), HttpStatus.OK);
        } else {
            return messageWithStatus("Cart with ID " + id + " does not exist", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Creates new {@link CartDTO}.
     *
     * @param cartDTO CartDTO to be added.
     * @return ResponseEntity with HTTP status and the cart being saved in database.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@Valid @RequestBody CartDTO cartDTO) {
        CartEntity cartEntity = cartMapper.toCartEntity(cartDTO);
        CartEntity cartEntityCreated = cartService.create(cartEntity);
        if (cartEntityCreated == null) {
            return messageWithStatus("User with ID " + cartDTO.getUserId() + " does not exist",
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(cartMapper.toCartDTO(cartEntityCreated), HttpStatus.CREATED);
    }

    /**
     * Updates {@link CartDTO}.
     *
     * @param id ID of cart to be updated.
     * @param cartDTO Object with data to update cart in database.
     * @return ResponseEntity with HTTP status and the cart being updated in database.
     */
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@PathVariable int id, @Valid @RequestBody CartDTO cartDTO) {
        CartEntity cartEntity = cartMapper.toCartEntity(cartDTO);
        CartEntity cartEntityUpdated = cartService.update(id, cartEntity);
        if (cartEntityUpdated != null) {
            return new ResponseEntity(cartMapper.toCartDTO(cartEntityUpdated), HttpStatus.OK);
        } else {
            return messageWithStatus("Cart with ID " + id + " does not exist", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes {@link CartDTO}.
     *
     * @param id ID of cart to be deleted.
     * @return ResponseEntity with HTTP status.
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@PathVariable int id) {
        boolean present = cartService.delete(id);
        if (present) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return messageWithStatus("Cart with ID " + id + " does not exist", HttpStatus.NOT_FOUND);
    }
}