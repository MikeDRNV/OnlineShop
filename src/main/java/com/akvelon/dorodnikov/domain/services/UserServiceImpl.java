package com.akvelon.dorodnikov.domain.services;

import com.akvelon.dorodnikov.domain.entites.CartEntity;
import com.akvelon.dorodnikov.domain.entites.UserDetails;
import com.akvelon.dorodnikov.domain.entites.UserEntity;
import com.akvelon.dorodnikov.domain.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

/**
 * Provides business logic for {@link }.
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity get(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public UserDetails getUserDetails(int userId) {
        UserDetails userDetails = new UserDetails();
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity != null) {
            userDetails.setUserEntity(userEntity);
            Optional<CartEntity> cartEntityOptional =
                    userEntity.getCartEntitySet().stream().filter(cart -> cart.isCompleted() == false).findFirst();
            if (cartEntityOptional.isPresent()) {
                userDetails.setIsActiveCart(true);
            } else {
                userDetails.setIsActiveCart(false);
            }
            return userDetails;
        }
        return null;
    }

    @Override
    public UserEntity create(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity update(Integer userId, UserEntity newUserEntity) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            userEntity.setFirstname(newUserEntity.getFirstname());
            userEntity.setLastname(newUserEntity.getLastname());
            userEntity.setEmail(newUserEntity.getEmail());
            userEntity.setPassword(newUserEntity.getPassword());
            userEntity.setDeleted(newUserEntity.isDeleted());
            userEntity.setCartEntitySet(newUserEntity.getCartEntitySet());
            return userRepository.save(userEntity);
        }
        return null;
    }

    @Override
    public UserEntity delete(Integer userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        user.setDeleted(true);
        user = userRepository.save(user);
        return user;
    }
}