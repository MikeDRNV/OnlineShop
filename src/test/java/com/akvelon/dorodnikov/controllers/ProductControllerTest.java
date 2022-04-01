package com.akvelon.dorodnikov.controllers;

import com.akvelon.dorodnikov.domain.entites.ProductEntity;
import com.akvelon.dorodnikov.domain.entites.ProductPagination;
import com.akvelon.dorodnikov.domain.services.ProductService;
import com.akvelon.dorodnikov.dto.ErrorDTO;
import com.akvelon.dorodnikov.dto.ProductDTO;
import com.akvelon.dorodnikov.dto.ProductPaginationDTO;
import com.akvelon.dorodnikov.utils.exceptions.PaginationException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationPageException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationSizeException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationSortException;
import com.akvelon.dorodnikov.utils.mappers.ProductMapper;
import com.akvelon.dorodnikov.utils.mappers.ProductPaginationMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest extends BaseControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    public static class TestConfig {

        static ProductService productService = Mockito.mock(ProductService.class);

        static ProductMapper productMapper = Mockito.mock(ProductMapper.class);

        static ProductPaginationMapper productPaginationMapper = Mockito.mock(ProductPaginationMapper.class);

        @Bean
        @Primary
        public ProductService productService() {
            return productService;
        }

        @Bean
        @Primary
        public ProductMapper productMapper() {
            return productMapper;
        }

        @Bean
        @Primary
        public ProductPaginationMapper productPaginationMapper() {
            return productPaginationMapper;
        }
    }

    @BeforeEach
    public void reset() {
        Mockito.reset(TestConfig.productService);
    }

    @Test
    public void getProductByID_getExistingProduct_200AndProductReturns() throws Exception {

        ProductDTO productDTO = new ProductDTO(1, "One", "s 1", "f 1", "link 1", new BigDecimal("11"));

        Mockito.when(TestConfig.productService.get(1))
                .thenReturn(new ProductEntity(1, "One", "s 1", "f 1", "link 1", new BigDecimal("11")));

        Mockito.when(TestConfig.productMapper.mapToProductDTO(any(ProductEntity.class))).thenReturn(productDTO);

        mockMvc.perform(get("/api/v1/products/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(productDTO.getName()))
                .andExpect(jsonPath("$.shortDescription").value(productDTO.getShortDescription()))
                .andExpect(jsonPath("$.fullDescription").value(productDTO.getFullDescription()))
                .andExpect(jsonPath("$.imgLink").value(productDTO.getImgLink()))
                .andExpect(jsonPath("$.id").value(productDTO.getId()))
                .andExpect(jsonPath("$.price").value(productDTO.getPrice()));

        Mockito.verify(TestConfig.productService).get(1);
    }

    @Test
    public void getProductByID_notExistingProduct_406() throws Exception {

        Mockito.when(TestConfig.productService.get(0)).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/products/0")).andExpect(status().isNotAcceptable())
                .andReturn();

        Mockito.verify(TestConfig.productService).get(0);

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("Product with ID 0 does not exist")));
    }

    @Test
    public void getProductPagination_validProduct_200AndProductPaginationReturns() throws Exception {

        ProductDTO firstProductDTO = new ProductDTO(1, "One", "s 1", "f 1", "link 1", new BigDecimal("11"));

        ProductDTO secondProductDTO = new ProductDTO(2, "Two", "s 2", "f 2", "link 2", new BigDecimal("22"));

        ProductPaginationDTO productPaginationDTO = new ProductPaginationDTO(2,
                Arrays.asList(firstProductDTO, secondProductDTO), 1, 0);

        Mockito.when(TestConfig.productService.getProductPagination(0, 2, "Asc", "name")).thenReturn(
                new ProductPagination(2,
                        Arrays.asList(new ProductEntity(1, "A", "s 1", "f 1", "link 1", new BigDecimal("11")),
                                new ProductEntity(2, "B", "s 2", "f 2", "link 2", new BigDecimal("22"))), 1, 0));

        Mockito.when(TestConfig.productPaginationMapper.mapToProductPaginationDTO(any(ProductPagination.class)))
                .thenReturn(productPaginationDTO);

        this.mockMvc.perform(get("/api/v1/products/pagination?page=0&size=2&sortDir=Asc&sort=name"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productPaginationDTO)));

        Mockito.verify(TestConfig.productService).getProductPagination(0, 2, "Asc", "name");
    }

    @Test
    public void getProductPagination_invalidSortParameter_400AndErrorMessage() throws Exception {

        Mockito.when(TestConfig.productService.getProductPagination(0, 2, "Asc", "null"))
                .thenThrow(new PaginationSortException("null"));

        MvcResult mvcResult = this.mockMvc.perform(
                        get("/api/v1/products/pagination?page=0&size=2&sortDir=Asc&sort" + "=null"))
                .andExpect(status().isBadRequest()).andReturn();

        Mockito.verify(TestConfig.productService).getProductPagination(0, 2, "Asc", "null");

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("The 'null' property was not found to sort the list")));
    }

    @Test
    public void getProductPagination_pageNumberIsMoreThanPossible_404AndErrorMessage() throws Exception {

        Mockito.when(TestConfig.productService.getProductPagination(1, 2, "Asc", "name"))
                .thenThrow(new PaginationPageException("1"));

        MvcResult mvcResult = this.mockMvc.perform(
                        get("/api/v1/products/pagination?page=1&size=2&sortDir=Asc&sort" + "=name"))
                .andExpect(status().isNotFound()).andReturn();

        Mockito.verify(TestConfig.productService).getProductPagination(1, 2, "Asc", "name");

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("Page number '1' not found")));
    }

    @Test
    public void getProductPagination_pageSizeIsMoreThanPossible_400AndErrorMessage() throws Exception {

        Mockito.when(TestConfig.productService.getProductPagination(0, 2, "Asc", "name"))
                .thenThrow(new PaginationSizeException("2"));

        MvcResult mvcResult = this.mockMvc.perform(
                        get("/api/v1/products/pagination?page=0&size=2&sortDir=Asc&sort" + "=name"))
                .andExpect(status().isBadRequest()).andReturn();

        Mockito.verify(TestConfig.productService).getProductPagination(0, 2, "Asc", "name");

        getListOfError(mvcResult,
                Arrays.asList(new ErrorDTO("Page size '2' is larger than the total number of " + "elements")));
    }

    @Test
    public void getProductPagination_invalidSortDirection_400AndErrorMessage() throws Exception {

        Mockito.when(TestConfig.productService.getProductPagination(1, 2, "null", "name")).thenThrow(
                new PaginationException("Invalid value 'null' for orders given! Has to be either "
                        + "'desc' or 'asc' (case insensitive)."));

        MvcResult mvcResult = this.mockMvc.perform(
                        get("/api/v1/products/pagination?page=1&size=2&sortDir=null&sort=name"))
                .andExpect(status().isBadRequest()).andReturn();

        Mockito.verify(TestConfig.productService).getProductPagination(1, 2, "null", "name");

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO(
                "Invalid value 'null' for orders given! Has to be either 'desc' or 'asc' (case insensitive).")));
    }

    @Test
    public void addProduct_validProduct_201AndSameProduct() throws Exception {

        ProductDTO productDTO = new ProductDTO(1, "One", "s 1", "f 1", "link 1", new BigDecimal("11"));

        Mockito.when(TestConfig.productMapper.mapToProductEntity(any(ProductDTO.class)))
                .thenReturn(new ProductEntity(1, "One", "s 1", "f 1", "link 1", new BigDecimal("11")));

        Mockito.when(TestConfig.productService.create(any(ProductEntity.class)))
                .thenReturn(new ProductEntity(1, "One", "s 1", "f 1", "link 1", new BigDecimal("11")));

        Mockito.when(TestConfig.productMapper.mapToProductDTO(any(ProductEntity.class))).thenReturn(productDTO);

        this.mockMvc.perform(post("/api/v1/products").content(objectMapper.writeValueAsString(productDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(productDTO)));

        Mockito.verify(TestConfig.productService).create(any(ProductEntity.class));
    }

    @Test
    public void addProduct_productWithEmptyField_400() throws Exception {

        ProductDTO productDTO = new ProductDTO(0, null, null, null, null, new BigDecimal("0"));

        Mockito.when(TestConfig.productService.create(any(ProductEntity.class))).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/api/v1/products").content(objectMapper.writeValueAsString(productDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("'imgLink' field is empty"),
                new ErrorDTO("'fullDescription' field is empty"), new ErrorDTO("'shortDescription' field is empty"),
                new ErrorDTO("'price' field should not be less than 1"), new ErrorDTO("'name' field is empty")));
    }

    @Test
    public void updateProduct_existingAndValidProduct_200AndPRODUCTSContainsUpdatedProduct() throws Exception {

        ProductDTO updatedProductDTO = new ProductDTO(1, "fix 1", "fix s 1", "fix f 1", "fix link 1",
                new BigDecimal("11"));

        Mockito.when(TestConfig.productMapper.mapToProductEntity(any(ProductDTO.class)))
                .thenReturn(new ProductEntity(1, "fix 1", "fix s 1", "fix f 1", "fix link 1", new BigDecimal("11")));

        Mockito.when(TestConfig.productService.update(eq(1), any(ProductEntity.class)))
                .thenReturn(new ProductEntity(1, "fix 1", "fix s 1", "fix f 1", "fix link 1", new BigDecimal("11")));

        Mockito.when(TestConfig.productMapper.mapToProductDTO(any(ProductEntity.class))).thenReturn(updatedProductDTO);

        this.mockMvc.perform(put("/api/v1/products/1").content(objectMapper.writeValueAsString(updatedProductDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedProductDTO)));

        Mockito.verify(TestConfig.productService).update(eq(1), any(ProductEntity.class));
    }

    @Test
    public void updateProduct_notExistingProduct_400() throws Exception {

        ProductDTO updatedProductDTO = new ProductDTO(-1, "n 1", "s 1", "f 1", "link 1", new BigDecimal("11"));

        Mockito.when(TestConfig.productMapper.mapToProductEntity(any(ProductDTO.class)))
                .thenReturn(new ProductEntity(-1, "n 1", "s 1", "f 1", "link 1", new BigDecimal("11")));

        Mockito.when(TestConfig.productService.update(eq(-1), any(ProductEntity.class))).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(
                put("/api/v1/products/-1").content(objectMapper.writeValueAsString(updatedProductDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        Mockito.verify(TestConfig.productService).update(eq(-1), any(ProductEntity.class));

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("Product with ID -1 does not exist")));
    }

    @Test
    public void updateProduct_existingIDAndInvalidProduct_400() throws Exception {

        ProductDTO productDTO = new ProductDTO(1, null, null, null, null, new BigDecimal("0"));

        Mockito.when(TestConfig.productService.update(eq(1), any(ProductEntity.class))).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(
                put("/api/v1/products/1").content(objectMapper.writeValueAsString(productDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("'imgLink' field is empty"),
                new ErrorDTO("'fullDescription' field is empty"), new ErrorDTO("'shortDescription' field is empty"),
                new ErrorDTO("'price' field should not be less than 1"), new ErrorDTO("'name' field is empty")));
    }

    @Test
    public void deleteProduct_existingProduct_204AndDBDoesNotContainDeletedProduct() throws Exception {

        Mockito.when(TestConfig.productService.delete(1)).thenReturn(true);

        this.mockMvc.perform(delete("/api/v1/products/1")).andExpect(status().isNoContent());

        Mockito.verify(TestConfig.productService).delete(1);
    }

    @Test
    public void deleteProduct_notExistingProduct_404() throws Exception {

        Mockito.when(TestConfig.productService.delete(0)).thenReturn(false);

        MvcResult mvcResult = this.mockMvc.perform(delete("/api/v1/products/0")).andExpect(status().isNotFound())
                .andReturn();

        Mockito.verify(TestConfig.productService).delete(0);

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("Product with ID 0 does not exist")));
    }
}