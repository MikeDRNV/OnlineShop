package com.akvelon.dorodnikov.domain.services;

import com.akvelon.dorodnikov.domain.entites.UserDetails;
import com.akvelon.dorodnikov.domain.entites.UserEntity;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Defines the behavior for {@link UserEntity}.
 */
@Service
public interface UserService {

    /**
     * Returns list of {@link UserEntity}.
     *
     * @return List of users.
     */
    List<UserEntity> getUsers();

    /**
     * Returns {@link UserEntity}.
     *
     * @param userId ID of user to be received.
     * @return UserEntity by ID.
     */
    UserEntity get(int userId);

    /**
     * Returns {@link UserDetails}.
     *
     * @param userId ID of user to be received.
     * @return User with status of his cart.
     */
    UserDetails getUserDetails(int userId);

    /**
     * Creates new {@link UserEntity}.
     *
     * @param userEntity UserEntity to be added.
     * @return UserEntity being saved in database.
     */
    UserEntity create(UserEntity userEntity);

    /**
     * Updates {@link UserEntity}.
     *
     * @param userId ID of user to be updated.
     * @param userEntity Object containing the data to update.
     * @return UserEntity being updated in database.
     */
    UserEntity update(Integer userId, UserEntity userEntity);

    /**
     * Marks {@link UserEntity} as deleted.
     *
     * @param userId ID of user to be deleted.
     * @return {@link UserEntity} marked as deleted or {@code null} if the user was not found.
     */
    UserEntity delete(Integer userId);
}