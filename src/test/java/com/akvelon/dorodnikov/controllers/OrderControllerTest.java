package com.akvelon.dorodnikov.controllers;

import com.akvelon.dorodnikov.domain.entites.OrderEntity;
import com.akvelon.dorodnikov.domain.services.OrderService;
import com.akvelon.dorodnikov.dto.ErrorDTO;
import com.akvelon.dorodnikov.dto.OrderDTO;

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

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest extends BaseControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    public static class TestConfig {

        static OrderService orderService = Mockito.mock(OrderService.class);

        @Bean
        @Primary
        public OrderService getOrderService() {
            return orderService;
        }
    }

    @Test
    public void getOrderByID_getExistingOrder_200AndOrderReturns() throws Exception {

        OrderDTO orderDTO = new OrderDTO(1, 1);

        Mockito.when(TestConfig.orderService.get(1)).thenReturn(new OrderEntity(1, 1));

        this.mockMvc.perform(get("/api/v1/orders/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderDTO.getId()))
                .andExpect(jsonPath("$.customerId").value(orderDTO.getCustomerId()));

        Mockito.verify(TestConfig.orderService).get(1);
    }

    @Test
    public void getOrderByID_notExistingOrder_406() throws Exception {

        Mockito.when(TestConfig.orderService.get(-1)).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/orders/-1")).andExpect(status().isNotAcceptable())
                .andReturn();

        Mockito.verify(TestConfig.orderService).get(-1);

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("Order with ID -1 does not exist")));
    }

    @Test
    public void addOrder_validOrder_201AndSameOrder() throws Exception {

        OrderDTO orderDTO = new OrderDTO(1, 3);

        Mockito.when(TestConfig.orderService.create(any(OrderEntity.class))).thenReturn(new OrderEntity(1, 3));

        this.mockMvc.perform(post("/api/v1/orders").content(objectMapper.writeValueAsString(orderDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(orderDTO)));

        Mockito.verify(TestConfig.orderService).create(any(OrderEntity.class));
    }

    @Test
    public void addOrder_orderWithEmptyField_400() throws Exception {

        Mockito.when(TestConfig.orderService.create(any(OrderEntity.class))).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(post("/api/v1/orders").content("{\"id\":null,\"customerId\":null}")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("'customerId' field is must not be null")));
    }
}