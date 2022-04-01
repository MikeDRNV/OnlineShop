package com.akvelon.dorodnikov.controllers;

import com.akvelon.dorodnikov.domain.CartProductPK;
import com.akvelon.dorodnikov.domain.entites.ActiveCart;
import com.akvelon.dorodnikov.domain.entites.CartEntity;
import com.akvelon.dorodnikov.domain.entites.CartItem;
import com.akvelon.dorodnikov.domain.entites.CartProduct;
import com.akvelon.dorodnikov.domain.entites.ProductEntity;
import com.akvelon.dorodnikov.domain.entites.UserEntity;
import com.akvelon.dorodnikov.domain.repositories.CartRepository;
import com.akvelon.dorodnikov.domain.services.CartService;
import com.akvelon.dorodnikov.dto.ActiveCartDTO;
import com.akvelon.dorodnikov.dto.CartDTO;
import com.akvelon.dorodnikov.dto.CartItemDTO;
import com.akvelon.dorodnikov.dto.ErrorDTO;
import com.akvelon.dorodnikov.dto.ProductQuantityDTO;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartActiveController.class)
class CartActiveControllerTest extends BaseControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CartRepository cartRepository;

    @TestConfiguration
    public static class TestConfig {
        static CartService cartService = Mockito.mock(CartService.class);

        @Bean
        @Primary
        public CartService cartService() {
            return cartService;
        }
    }

    @BeforeEach
    public void reset() {
        Mockito.reset(TestConfig.cartService);
    }

    @Test
    void getListOfProductsInCart_notEmptyCart_200() throws Exception {

        ActiveCartDTO activeCartDTO = new ActiveCartDTO(1, 1,
                Arrays.asList(new CartItemDTO(1, "n1", "d1", "l1", 1, new BigDecimal("1")),
                        new CartItemDTO(2, "n2", "d2", "l2", 2, new BigDecimal("2"))));

        Mockito.when(TestConfig.cartService.getProductsInCart(1)).thenReturn(new ActiveCart(1, 1,
                Arrays.asList(new CartItem(1, "n1", "d1", "l1", 1, new BigDecimal("1")),
                        new CartItem(2, "n2", "d2", "l2", 2, new BigDecimal("2")))));

        this.mockMvc.perform(get("/api/v1/cart/active/1/products")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(activeCartDTO)));

        Mockito.verify(TestConfig.cartService).getProductsInCart(1);
    }

    @Test
    void getListOfProductsInCart_emptyCart_200() throws Exception {

        ActiveCartDTO activeCartDTO = new ActiveCartDTO(1, 0, new ArrayList<>());

        Mockito.when(TestConfig.cartService.getProductsInCart(0)).thenReturn(new ActiveCart(1, 0, new ArrayList<>()));

        this.mockMvc.perform(get("/api/v1/cart/active/0/products")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(activeCartDTO)));

        Mockito.verify(TestConfig.cartService).getProductsInCart(0);
    }

    @Test
    void getListOfProductsInCart_notActiveCart_400AndErrorMessage() throws Exception {

        Mockito.when(TestConfig.cartService.getProductsInCart(0)).thenReturn(null);

        MvcResult result = this.mockMvc.perform(get("/api/v1/cart/active/0/products"))
                .andExpect(status().isNotFound()).andReturn();

        Mockito.verify(TestConfig.cartService).getProductsInCart(0);

        getListOfError(result, Arrays.asList(new ErrorDTO("The user with ID 0 does not have active cart")));
    }

    @Test
    void addProductToCart_createCartAndQuantityEquals1_201() throws Exception {

        ProductQuantityDTO productQuantityDTO = new ProductQuantityDTO(1, 1);

        ActiveCartDTO activeCartDTO = new ActiveCartDTO(1, 1,
                Arrays.asList(new CartItemDTO(1, "n1", "d1", "l1", 1, new BigDecimal("1"))));

        Mockito.when(TestConfig.cartService.addProductToActive(1, 1, 1)).thenReturn(
                new ActiveCart(1, 1, Arrays.asList(new CartItem(1, "n1", "d1", "l1", 1, new BigDecimal("1")))));
        this.mockMvc.perform(post("/api/v1/cart/active/1/changeProductQuantity").content(
                        objectMapper.writeValueAsString(productQuantityDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(activeCartDTO)));

        Mockito.verify(TestConfig.cartService).addProductToActive(1, 1, 1);
    }

    @Test
    void addProductToCart_productWithQuantityEquals0_201ReturnEmptyCart() throws Exception {

        ProductQuantityDTO productQuantityDTO = new ProductQuantityDTO(1, 0);

        ActiveCartDTO activeCartDTO = new ActiveCartDTO(1, 1, new ArrayList<>());

        Mockito.when(TestConfig.cartService.addProductToActive(1, 1, 0))
                .thenReturn(new ActiveCart(1, 1, new ArrayList<>()));

        this.mockMvc.perform(post("/api/v1/cart/active/1/changeProductQuantity").content(
                        objectMapper.writeValueAsString(productQuantityDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(activeCartDTO)));

        Mockito.verify(TestConfig.cartService).addProductToActive(1, 1, 0);
    }

    @Test
    void addProductToCart_productNotExist_400ErrorMessage() throws Exception {

        ProductQuantityDTO productQuantityDTO = new ProductQuantityDTO(1, 1);

        Mockito.when(TestConfig.cartService.addProductToActive(1, 1, 1)).thenReturn(null);
        MvcResult result = this.mockMvc.perform(post("/api/v1/cart/active/1/changeProductQuantity").content(
                        objectMapper.writeValueAsString(productQuantityDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();
        Mockito.verify(TestConfig.cartService).addProductToActive(1, 1, 1);

        getListOfError(result, Arrays.asList(new ErrorDTO("Product with ID 1 does not exist")));
    }

    @Test
    void addProductToCart_negativeValues_400ErrorMessage() throws Exception {

        ProductQuantityDTO productQuantityDTO = new ProductQuantityDTO(-1, -1);

        Mockito.when(TestConfig.cartService.addProductToActive(1, -1, -1)).thenReturn(null);
        MvcResult result = this.mockMvc.perform(post("/api/v1/cart/active/1/changeProductQuantity").content(
                        objectMapper.writeValueAsString(productQuantityDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        getListOfError(result, Arrays.asList(new ErrorDTO("'productId' field should not be less than 0"),
                new ErrorDTO("'quantity' field should not be less than 0")));
    }

    @Test
    void makeOrder_validValues_200() throws Exception {

        CartDTO orderDTO = new CartDTO(1, 0, true);

        UserEntity user = new UserEntity();
        user.setId(0);

        CartEntity order = new CartEntity(1, user, true, new ArrayList<>());

        ProductEntity productEntity = new ProductEntity(1, "n", "s", "f", "l", new BigDecimal("1"));
        CartProduct cartProduct = new CartProduct(new CartProductPK(1, 1), productEntity, order, 1,
                new BigDecimal("1"));

        Mockito.when(TestConfig.cartService.makeOrder(1))
                .thenReturn(new CartEntity(1, user, true, Arrays.asList(cartProduct)));

        this.mockMvc.perform(patch("/api/v1/cart/active/1/order").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(orderDTO)));

        Mockito.verify(TestConfig.cartService).makeOrder(1);
    }

    @Test
    void makeOrder_emptyCart_404ErrorMessage() throws Exception {

        UserEntity user = new UserEntity();
        user.setId(1);

        Mockito.when(TestConfig.cartService.makeOrder(1)).thenReturn(new CartEntity(1, user, false, new ArrayList<>()));

        MvcResult result = this.mockMvc.perform(
                        patch("/api/v1/cart/active/1/order").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

        Mockito.verify(TestConfig.cartService).makeOrder(1);

        getListOfError(result, Arrays.asList(new ErrorDTO("The user with ID 1 has an empty shopping cart")));
    }

    @Test
    void makeOrder_cartNotExist_400ErrorMessage() throws Exception {

        Mockito.when(TestConfig.cartService.makeOrder(1)).thenReturn(null);

        MvcResult result = this.mockMvc.perform(
                        patch("/api/v1/cart/active/1/order").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        Mockito.verify(TestConfig.cartService).makeOrder(1);

        getListOfError(result, Arrays.asList(new ErrorDTO("The user with ID 1 does not have active cart")));
    }
}