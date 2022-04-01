package com.akvelon.dorodnikov.controllers;

import com.akvelon.dorodnikov.controllers.helpers.BaseController;
import com.akvelon.dorodnikov.domain.entites.UserDetails;
import com.akvelon.dorodnikov.domain.entites.UserEntity;
import com.akvelon.dorodnikov.domain.services.UserService;
import com.akvelon.dorodnikov.dto.UserDTO;
import com.akvelon.dorodnikov.dto.UserDetailsDTO;
import com.akvelon.dorodnikov.utils.mappers.UserDetailsMapper;
import com.akvelon.dorodnikov.utils.mappers.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for "User" domain entity requests.
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final UserDetailsMapper userDetailsMapper;

    /**
     * Returns ResponseEntity with HTTP status and list of {@link UserDTO}-s.
     *
     * @return ResponseEntity with HTTP status and list of users.
     */
    @GetMapping
    public ResponseEntity getUsers() {
        List<UserEntity> userEntities = userService.getUsers();
        if (userEntities != null) {
            return new ResponseEntity(userMapper.mapToUserDTOList(userEntities), HttpStatus.OK);
        }
        return new ResponseEntity("The list of users was not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Returns ResponseEntity with HTTP status and {@link UserDTO}.
     *
     * @param id ID of user to be received.
     * @return ResponseEntity with HTTP status and UserDTO by ID.
     */
    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable int id) {
        UserEntity userEntity = userService.get(id);
        if (userEntity != null) {
            return new ResponseEntity(userMapper.userEntityToUserDTO(userEntity), HttpStatus.OK);
        }
        return messageWithStatus("User with ID " + id + " does not exist", HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Returns ResponseEntity with HTTP status and {@link UserDetailsDTO}.
     *
     * @param userId ID of user to be received.
     * @return ResponseEntity with HTTP status and UserDetailsDTO by ID.
     */
    @GetMapping("/{userId}/details")
    public ResponseEntity getUserDetails(@PathVariable int userId) {
        UserDetails userDetails = userService.getUserDetails(userId);
        if (userDetails == null) {
            return messageWithStatus("User with ID " + userId + " does not exist", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(userDetailsMapper.mapToUserDetailsDTO(userDetails), HttpStatus.OK);
    }

    /**
     * Creates new {@link UserDTO}.
     *
     * @param userDTO UserDTO to be added.
     * @return ResponseEntity with HTTP status and the user being saved in database.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@Valid @RequestBody UserDTO userDTO) {
        UserEntity userEntity = userMapper.userDTOToUserEntity(userDTO);
        UserEntity createdUserEntity = userService.create(userEntity);
        return new ResponseEntity(userMapper.userEntityToUserDTO(userEntity), HttpStatus.CREATED);
    }

    /**
     * Updates {@link UserDTO}.
     *
     * @param id ID of user to be updated.
     * @param userDTO Object with data to update user in database.
     * @return ResponseEntity with HTTP status and the user being updated in database.
     */
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@PathVariable int id, @Valid @RequestBody UserDTO userDTO) {
        UserEntity userEntity = userMapper.userDTOToUserEntity(userDTO);
        UserEntity updatedUserEntity = userService.update(id, userEntity);
        if (updatedUserEntity != null) {
            return new ResponseEntity(userMapper.userEntityToUserDTO(updatedUserEntity), HttpStatus.OK);
        }
        return messageWithStatus("User with ID " + id + " does not exist", HttpStatus.BAD_REQUEST);
    }

    /**
     * Deletes {@link UserDTO}.
     *
     * @param id ID of Product to be deleted.
     * @return ResponseEntity with HTTP status.
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@PathVariable int id) {
        UserEntity userEntity = userService.delete(id);
        if (userEntity != null) {
            return new ResponseEntity(userMapper.userEntityToUserDTO(userEntity), HttpStatus.OK);
        } else {
            return messageWithStatus("User with ID " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
    }
}