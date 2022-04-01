package com.akvelon.dorodnikov.controllers;

import com.akvelon.dorodnikov.domain.CartProductPK;
import com.akvelon.dorodnikov.domain.entites.CartEntity;
import com.akvelon.dorodnikov.domain.entites.CartProduct;
import com.akvelon.dorodnikov.domain.entites.ProductEntity;
import com.akvelon.dorodnikov.domain.entites.UserEntity;
import com.akvelon.dorodnikov.domain.repositories.CartRepository;
import com.akvelon.dorodnikov.domain.services.CartService;
import com.akvelon.dorodnikov.dto.CartDTO;
import com.akvelon.dorodnikov.dto.ErrorDTO;
import com.akvelon.dorodnikov.utils.exceptions.NotEmptyCartDeletionException;
import com.akvelon.dorodnikov.utils.mappers.CartMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest extends BaseControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CartRepository cartRepository;

    @TestConfiguration
    public static class TestConfig {

        static CartService cartService = Mockito.mock(CartService.class);

        static CartMapper cartMapper = Mockito.mock(CartMapper.class);

        @Bean
        @Primary
        public CartService cartService() {
            return cartService;
        }

        @Bean
        @Primary
        public CartMapper cartMapper() {
            return cartMapper;
        }
    }

    @BeforeEach
    public void reset() {
        Mockito.reset(TestConfig.cartService);
    }

    @Test
    public void getCartByID_getExistingCart_200AndCartReturns() throws Exception {

        CartDTO cartDTO = new CartDTO(1, 0, false);

        UserEntity user = new UserEntity(0, "fn", "ln", "em", "pass", false, new HashSet<>());

        Mockito.when(TestConfig.cartService.get(1)).thenReturn(new CartEntity(1, user, false, new ArrayList<>()));

        Mockito.when(TestConfig.cartMapper.toCartDTO(any(CartEntity.class))).thenReturn(cartDTO);

        this.mockMvc.perform(get("/api/v1/cart/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartDTO.getId()))
                .andExpect(jsonPath("$.userId").value(cartDTO.getUserId()))
                .andExpect(jsonPath("$.isCompleted").value(cartDTO.getIsCompleted()));

        Mockito.verify(TestConfig.cartService).get(1);
    }

    @Test
    public void getCartByID_notExistingCart_406() throws Exception {

        Mockito.when(TestConfig.cartService.get(-1)).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/cart/-1")).andExpect(status().isNotAcceptable())
                .andReturn();

        Mockito.verify(TestConfig.cartService).get(-1);

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("Cart with ID -1 does not exist")));
    }

    @Test
    public void addCart_validCart_201AndSameCart() throws Exception {

        CartDTO cartDTO = new CartDTO(1, 1, false);

        UserEntity user = new UserEntity();
        user.setId(1);

        Mockito.when(TestConfig.cartMapper.toCartEntity(any(CartDTO.class)))
                .thenReturn(new CartEntity(1, user, false, new ArrayList<>()));

        Mockito.when(TestConfig.cartService.create(any(CartEntity.class)))
                .thenReturn(new CartEntity(1, user, false, new ArrayList<>()));

        Mockito.when(TestConfig.cartMapper.toCartDTO(any(CartEntity.class))).thenReturn(cartDTO);

        this.mockMvc.perform(post("/api/v1/cart").content(objectMapper.writeValueAsString(cartDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(cartDTO)));

        Mockito.verify(TestConfig.cartService).create(any(CartEntity.class));
    }

    @Test
    public void addCart_cartWithEmptyField_400() throws Exception {

        Mockito.when(TestConfig.cartService.create(any(CartEntity.class))).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/api/v1/cart").content("{\"id\":null,\"isCompleted\":null,\"userId\":null}")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("'isCompleted' field is must not be null"),
                new ErrorDTO("'userId' field is must not be null")));
    }

    @Test
    public void updateCart_existingAndValidCart_200AndCartUpdated() throws Exception {

        CartDTO cartDTO = new CartDTO(1, 1, false);

        UserEntity user = new UserEntity();
        user.setId(1);

        Mockito.when(TestConfig.cartMapper.toCartEntity(any(CartDTO.class)))
                .thenReturn(new CartEntity(1, user, false, new ArrayList<>()));

        Mockito.when(TestConfig.cartService.update(eq(1), any(CartEntity.class)))
                .thenReturn(new CartEntity(1, user, false, new ArrayList<>()));

        Mockito.when(TestConfig.cartMapper.toCartDTO(any(CartEntity.class))).thenReturn(cartDTO);

        this.mockMvc.perform(put("/api/v1/cart/1").content(objectMapper.writeValueAsString(cartDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cartDTO)));

        Mockito.verify(TestConfig.cartService).update(eq(1), any(CartEntity.class));
    }

    @Test
    public void updateCart_notExistingCart_400() throws Exception {

        CartDTO cartDTO = new CartDTO(-1, 1, false);

        UserEntity user = new UserEntity();
        user.setId(1);

        Mockito.when(TestConfig.cartMapper.toCartEntity(cartDTO))
                .thenReturn(new CartEntity(-1, user, false, new ArrayList<>()));

        Mockito.when(TestConfig.cartService.update(eq(-1), any(CartEntity.class))).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(
                put("/api/v1/cart/-1").content(objectMapper.writeValueAsString(cartDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        Mockito.verify(TestConfig.cartService).update(eq(-1), any(CartEntity.class));
        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("Cart with ID -1 does not exist")));
    }

    @Test
    public void updateCart_existingIDAndInvalidCart_400() throws Exception {

        CartDTO cartDTO = new CartDTO(1, null, null);

        Mockito.when(TestConfig.cartService.update(eq(1), any(CartEntity.class))).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(
                put("/api/v1/cart/1").content(objectMapper.writeValueAsString(cartDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("'userId' field is must not be null"),
                new ErrorDTO("'isCompleted' field is must not be null")));
    }

    @Test
    public void deleteCart_emptyCart_204AndCartServiceDoesNotContainDeletedCart() throws Exception {

        UserEntity user = new UserEntity();
        user.setId(0);

        CartEntity emptyCart = new CartEntity(1, user, false, new ArrayList<>());

        Mockito.when(cartRepository.save(emptyCart)).thenReturn(new CartEntity(1, user, false, new ArrayList<>()));
        cartRepository.save(emptyCart);
        Mockito.verify(cartRepository).save(emptyCart);

        Mockito.when(TestConfig.cartService.delete(emptyCart.getId())).thenReturn(true);

        this.mockMvc.perform(delete("/api/v1/cart/1")).andExpect(status().isNoContent());

        Mockito.verify(TestConfig.cartService).delete(1);
    }

    @Test
    public void deleteCart_existingNotEmptyCart_400AndErrorMessage() throws Exception {

        UserEntity user = new UserEntity();
        user.setId(0);

        CartEntity cartEntity = new CartEntity(1, user, false, new ArrayList<>());

        CartProduct cartProduct = new CartProduct(new CartProductPK(1, 1), new ProductEntity(), cartEntity, 1,
                new BigDecimal("1"));

        cartEntity.setCartProductList(Arrays.asList(cartProduct));

        Mockito.when(cartRepository.save(cartEntity)).thenReturn(new CartEntity(1, user, false, Arrays.asList(
                new CartProduct(new CartProductPK(1, 1), new ProductEntity(), cartEntity, 1, new BigDecimal("1")))));
        cartRepository.save(cartEntity);
        Mockito.verify(cartRepository).save(cartEntity);

        Exception cause = new RuntimeException("ERROR: update or delete on table \"carts\" violates foreign key "
                + "constraint \"cart_to_cart_products_relation\" on table \"cart_product\"\n  Detail: Key (id)=(1) is "
                + "still referenced from table \"cart_product\".");

        Mockito.when(TestConfig.cartService.delete(cartEntity.getId())).thenThrow(
                new NotEmptyCartDeletionException("The cart can't be deleted because it's not empty. ", cause));

        MvcResult mvcResult = this.mockMvc.perform(delete("/api/v1/cart/1")).andExpect(status().isBadRequest())
                .andReturn();

        Mockito.verify(TestConfig.cartService).delete(1);

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("The cart can't be deleted because it's not empty. "
                + "ERROR: update or delete on table \"carts\" violates foreign key constraint "
                + "\"cart_to_cart_products_relation\" on table \"cart_product\"\n  Detail: Key (id)=(1) is "
                + "still referenced from table \"cart_product\".")));
    }

    @Test
    public void deleteCart_notExistingCart_404AndErrorMessage() throws Exception {

        Mockito.when(TestConfig.cartService.delete(-1)).thenReturn(false);

        MvcResult mvcResult = this.mockMvc.perform(delete("/api/v1/cart/-1")).andExpect(status().isNotFound())
                .andReturn();

        Mockito.verify(TestConfig.cartService).delete(-1);

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("Cart with ID -1 does not exist")));
    }
}