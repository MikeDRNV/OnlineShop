package com.akvelon.dorodnikov.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User model DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private int id;

    @NotEmpty(message = "'firstname' field is empty")
    private String firstname;

    @NotEmpty(message = "'lastname' field is empty")
    private String lastname;

    @NotEmpty(message = "'email' field is empty")
    private String email;

    @NotEmpty(message = "'password' field is empty")
    private String password;

    @NotNull(message = "'isDeleted' field is must not be null")
    private Boolean isDeleted;
}