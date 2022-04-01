package com.akvelon.dorodnikov.utils.mappers;

import com.akvelon.dorodnikov.domain.entites.ProductPagination;
import com.akvelon.dorodnikov.dto.ProductPaginationDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Provides conversion for {@link ProductPagination} to {@link ProductPaginationDTO}.
 */
@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface ProductPaginationMapper {

    /**
     * Converts {@link ProductPagination} to {@link ProductPaginationDTO}.
     *
     * @param productPagination Provides data for conversion.
     * @return {@link ProductPaginationDTO} object.
     */
    @Mapping(target = "productDTOS", source = "productEntities")
    ProductPaginationDTO mapToProductPaginationDTO(ProductPagination productPagination);
}