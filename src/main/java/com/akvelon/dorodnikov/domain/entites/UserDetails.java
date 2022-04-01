package com.akvelon.dorodnikov.domain.entites;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User details model entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {

    @NotNull(message = "'userEntity' field is must not be null")
    private UserEntity userEntity;

    @NotNull(message = "'isActiveCart' field is must not be null")
    private Boolean isActiveCart;
}