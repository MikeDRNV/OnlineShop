package com.akvelon.dorodnikov.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product model DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private int id;

    @NotEmpty(message = "'name' field is empty")
    private String name;

    @NotEmpty(message = "'shortDescription' field is empty")
    private String shortDescription;

    @NotEmpty(message = "'fullDescription' field is empty")
    private String fullDescription;

    @NotEmpty(message = "'imgLink' field is empty")
    private String imgLink;

    @Min(value = 1, message = "'price' field should not be less than 1")
    private BigDecimal price;
}