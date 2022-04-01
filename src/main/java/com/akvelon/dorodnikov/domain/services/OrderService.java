package com.akvelon.dorodnikov.domain.services;

import com.akvelon.dorodnikov.domain.entites.OrderEntity;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Defines the behavior for {@link OrderEntity}.
 */
@Service
public interface OrderService {

    /**
     * Returns list of {@link OrderEntity}-s.
     *
     * @param page Number of page.
     * @param size Limit of quantity of elements on one page.
     * @return List of orders.
     */
    List<OrderEntity> getOrders(int page, int size);

    /**
     * Returns {@link OrderEntity}.
     *
     * @param id ID of OrderEntity to be received.
     * @return OrderEntity by ID.
     */
    OrderEntity get(int id);

    /**
     * Creates new {@link OrderEntity}.
     *
     * @param orderEntity OrderEntity to be added.
     * @return OrderEntity being saved in database.
     */
    OrderEntity create(OrderEntity orderEntity);
}