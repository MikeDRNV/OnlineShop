package com.akvelon.dorodnikov.dto;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Active cart model DTO.
 */
@Data
@AllArgsConstructor
public class ActiveCartDTO {

    private int id;

    @Min(value = 0, message = "'userId' field should not be less than 0")
    @NotNull(message = "'userId' field is must not be null")
    private Integer userId;

    @NotNull(message = "'cartItemDTOs' field is must not be null")
    List<CartItemDTO> cartItemDTOs;
}