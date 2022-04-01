package com.akvelon.dorodnikov.domain.repositories;

import com.akvelon.dorodnikov.domain.entites.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides CRUD methods for {@link UserEntity}.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {}