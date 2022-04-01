package com.akvelon.dorodnikov.domain.services;

import com.akvelon.dorodnikov.domain.entites.OrderEntity;
import com.akvelon.dorodnikov.domain.repositories.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * Provides business logic for {@link OrderEntity}.
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderEntity> getOrders(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderEntity> orders = orderRepository.findAll(pageRequest);
        return orders.getContent();
    }

    @Override
    public OrderEntity get(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public OrderEntity create(OrderEntity orderEntity) {
        return orderRepository.save(orderEntity);
    }
}