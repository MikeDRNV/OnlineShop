package com.akvelon.dorodnikov.domain.repositories;

import com.akvelon.dorodnikov.domain.entites.OrderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides CRUD methods for {@link OrderEntity}.
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {}