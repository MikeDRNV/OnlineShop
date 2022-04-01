package com.akvelon.dorodnikov.controllers;

import com.akvelon.dorodnikov.domain.entites.CartEntity;
import com.akvelon.dorodnikov.domain.entites.UserDetails;
import com.akvelon.dorodnikov.domain.entites.UserEntity;
import com.akvelon.dorodnikov.domain.services.UserService;
import com.akvelon.dorodnikov.dto.ErrorDTO;
import com.akvelon.dorodnikov.dto.UserDTO;
import com.akvelon.dorodnikov.dto.UserDetailsDTO;
import com.akvelon.dorodnikov.utils.mappers.UserDetailsMapper;
import com.akvelon.dorodnikov.utils.mappers.UserMapper;

import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest extends BaseControllersTest {

    @Autowired
    MockMvc mockMvc;

    @TestConfiguration
    public static class TestConfig {

        static UserService userService = Mockito.mock(UserService.class);

        static UserMapper userMapper = Mockito.mock(UserMapper.class);

        static UserDetailsMapper userDetailsMapper = Mockito.mock(UserDetailsMapper.class);

        @Bean
        @Primary
        public UserService userService() {
            return userService;
        }

        @Bean
        @Primary
        public UserMapper userMapper() {
            return userMapper;
        }

        @Bean
        @Primary
        public UserDetailsMapper userDetailsMapper() {
            return userDetailsMapper;
        }
    }

    @BeforeEach
    public void reset() {
        Mockito.reset(TestConfig.userService);
    }

    @Test
    void getUserByID_existingUser_200AndUserReturns() throws Exception {

        UserDTO userDTO = new UserDTO(1, "fn", "ln", "em", "pass", false);

        Mockito.when(TestConfig.userService.get(1))
                .thenReturn(new UserEntity(1, "fn", "ln", "em", "pass", false, new HashSet<>()));

        Mockito.when(TestConfig.userMapper.userEntityToUserDTO(any(UserEntity.class))).thenReturn(userDTO);

        this.mockMvc.perform(get("/api/v1/users/1")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));

        Mockito.verify(TestConfig.userService).get(1);
    }

    @Test
    void getUserByID_notExistingUser_406AndErrorMessage() throws Exception {

        Mockito.when(TestConfig.userService.get(-1)).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/users/-1")).andExpect(status().isNotAcceptable())
                .andReturn();

        Mockito.verify(TestConfig.userService).get(-1);

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("User with ID -1 does not exist")));
    }

    @Test
    void getUserDetails_existingUser_200AndUserDetailsReturns() throws Exception {

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(1, "fn", "ls", "em", "pass", false, true);

        UserEntity userEntity = new UserEntity(1, "fn", "ls", "em", "pass", false, new HashSet<>());

        CartEntity cartEntity = new CartEntity(1, userEntity, false, new ArrayList<>());

        userEntity.setCartEntitySet(new HashSet<>(Arrays.asList(cartEntity)));

        Mockito.when(TestConfig.userService.getUserDetails(1)).thenReturn(new UserDetails(userEntity, true));

        Mockito.when(TestConfig.userDetailsMapper.mapToUserDetailsDTO(any(UserDetails.class)))
                .thenReturn(userDetailsDTO);

        this.mockMvc.perform(get("/api/v1/users/1/details")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDetailsDTO)));

        Mockito.verify(TestConfig.userService).getUserDetails(1);
    }

    @Test
    void getUserDetails_notExistingUser_406AndErrorMessage() throws Exception {

        Mockito.when(TestConfig.userService.getUserDetails(-1)).thenReturn(null);

        MvcResult mvcResult =
                this.mockMvc.perform(get("/api/v1/users/-1/details")).andExpect(status().isNotAcceptable())
                .andReturn();

        Mockito.verify(TestConfig.userService).getUserDetails(-1);

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("User with ID -1 does not exist")));
    }

    @Test
    void addNewUser_validUser_201AndSameUser() throws Exception {

        UserDTO userDTO = new UserDTO(1, "fn", "ln", "em", "pass", false);

        Mockito.when(TestConfig.userMapper.userDTOToUserEntity(any(UserDTO.class)))
                .thenReturn(new UserEntity(1, "fn", "ln", "em", "pass", false, new HashSet<>()));

        Mockito.when(TestConfig.userService.create(any(UserEntity.class)))
                .thenReturn(new UserEntity(1, "fn", "ln", "em", "pass", false, new HashSet<>()));

        Mockito.when(TestConfig.userMapper.userEntityToUserDTO(any(UserEntity.class))).thenReturn(userDTO);

        this.mockMvc.perform(post("/api/v1/users").content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));

        Mockito.verify(TestConfig.userService).create(any(UserEntity.class));
    }

    @Test
    void addNewUser_invalidUser_400AndErrorMessage() throws Exception {

        UserDTO userDTO = new UserDTO(0, null, null, null, null, null);

        Mockito.when(TestConfig.userService.create(any(UserEntity.class))).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/api/v1/users").content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        getListOfError(mvcResult,
                Arrays.asList(new ErrorDTO("'firstname' field is empty"), new ErrorDTO("'lastname' field is empty"),
                        new ErrorDTO("'email' field is empty"), new ErrorDTO("'password' field is empty"),
                        new ErrorDTO("'isDeleted' field is must not be null")));
    }

    @Test
    public void updateUserByID_existingAndValidUser_200AndUserUpdated() throws Exception {

        UserDTO userDTO = new UserDTO(1, "fn", "ln", "em", "pass", false);

        Mockito.when(TestConfig.userMapper.userDTOToUserEntity(any(UserDTO.class)))
                .thenReturn(new UserEntity(1, "fn", "ln", "em", "pass", false, new HashSet<>()));

        Mockito.when(TestConfig.userService.update(eq(1), any(UserEntity.class)))
                .thenReturn(new UserEntity(1, "fn", "ln", "em", "pass", false, new HashSet<>()));

        Mockito.when(TestConfig.userMapper.userEntityToUserDTO(any(UserEntity.class))).thenReturn(userDTO);

        this.mockMvc.perform(put("/api/v1/users/1").content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));

        Mockito.verify(TestConfig.userService).update(eq(1), any(UserEntity.class));
    }

    @Test
    public void updateUserByID_notExistingUser_400AndErrorMessage() throws Exception {

        UserDTO userDTO = new UserDTO(-1, "fn", "ln", "em", "pass", false);

        Mockito.when(TestConfig.userMapper.userDTOToUserEntity(any(UserDTO.class)))
                .thenReturn(new UserEntity(-1, "fn", "ln", "em", "pass", false, new HashSet<>()));

        Mockito.when(TestConfig.userService.update(eq(-1), any(UserEntity.class))).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(
                put("/api/v1/users/-1").content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        Mockito.verify(TestConfig.userService).update(eq(-1), any(UserEntity.class));

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("User with ID -1 does not exist")));
    }

    @Test
    public void updateUserByID_existingIDAndInvalidUser_400AndErrorMessage() throws Exception {

        UserDTO userDTO = new UserDTO(0, null, null, null, null, null);

        Mockito.when(TestConfig.userService.update(eq(0), any(UserEntity.class))).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(
                put("/api/v1/users/0").content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();

        getListOfError(mvcResult,
                Arrays.asList(new ErrorDTO("'firstname' field is empty"), new ErrorDTO("'lastname' field is empty"),
                        new ErrorDTO("'email' field is empty"), new ErrorDTO("'password' field is empty"),
                        new ErrorDTO("'isDeleted' field is must not be null")));
    }

    @Test
    void deleteUserByID_existingUser_200() throws Exception {

        UserDTO userDTO = new UserDTO(1, "fn", "ln", "em", "pass", true);

        Mockito.when(TestConfig.userService.delete(1))
                .thenReturn(new UserEntity(1, "fn", "ln", "em", "pass", true, new HashSet<>()));

        Mockito.when(TestConfig.userMapper.userEntityToUserDTO(any(UserEntity.class))).thenReturn(userDTO);

        this.mockMvc.perform(delete("/api/v1/users/1")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));

        Mockito.verify(TestConfig.userService).delete(1);
    }

    @Test
    void deleteUserByID_notExistingUser_404AndErrorMessage() throws Exception {

        Mockito.when(TestConfig.userService.delete(-1)).thenReturn(null);

        MvcResult mvcResult = this.mockMvc.perform(delete("/api/v1/users/-1")).andExpect(status().isNotFound())
                .andReturn();

        Mockito.verify(TestConfig.userService).delete(-1);

        getListOfError(mvcResult, Arrays.asList(new ErrorDTO("User with ID -1 does not exist")));
    }
}