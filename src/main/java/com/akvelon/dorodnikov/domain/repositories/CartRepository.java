package com.akvelon.dorodnikov.domain.repositories;

import com.akvelon.dorodnikov.domain.entites.CartEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides CRUD methods for {@link CartEntity}.
 */
@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {
    CartEntity findByUserIdAndIsCompleted(int id, boolean isCompleted);
}