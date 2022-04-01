package com.akvelon.dorodnikov.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product pagination model DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPaginationDTO {

    long totalItems;

    List<ProductDTO> productDTOS;

    int totalPages;

    int currentPage;
}