package com.akvelon.dorodnikov.domain.entites;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product pagination model entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPagination {

    long totalItems;

    List<ProductEntity> productEntities;

    int totalPages;

    int currentPage;
}