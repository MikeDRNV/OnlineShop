package com.akvelon.dorodnikov.domain.repositories;

import com.akvelon.dorodnikov.domain.entites.ProductEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides CRUD methods for {@link ProductEntity}.
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {}