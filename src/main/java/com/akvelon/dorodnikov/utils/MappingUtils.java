package com.akvelon.dorodnikov.utils;

import com.akvelon.dorodnikov.domain.entites.ActiveCart;
import com.akvelon.dorodnikov.domain.entites.CartEntity;
import com.akvelon.dorodnikov.domain.entites.CartItem;
import com.akvelon.dorodnikov.domain.entites.CartProduct;
import com.akvelon.dorodnikov.domain.entites.OrderEntity;
import com.akvelon.dorodnikov.domain.entites.ProductEntity;
import com.akvelon.dorodnikov.domain.entites.ProductPagination;
import com.akvelon.dorodnikov.domain.entites.UserEntity;
import com.akvelon.dorodnikov.dto.ActiveCartDTO;
import com.akvelon.dorodnikov.dto.CartDTO;
import com.akvelon.dorodnikov.dto.CartItemDTO;
import com.akvelon.dorodnikov.dto.CartProductDTO;
import com.akvelon.dorodnikov.dto.OrderDTO;
import com.akvelon.dorodnikov.dto.ProductDTO;
import com.akvelon.dorodnikov.dto.ProductPaginationDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides object conversion.
 */
public class MappingUtils {

    /**
     * Converts a {@link ProductEntity} to {@link ProductDTO} based on the productEntity object.
     *
     * @param productEntity Provides data for conversion.
     * @return ProductDTO object based on the object of {@link ProductEntity} in the parameter.
     */
    public static ProductDTO mapToProduct(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }
        return new ProductDTO(productEntity.getId(), productEntity.getName(), productEntity.getShortDescription(),
                productEntity.getFullDescription(), productEntity.getImgLink(), productEntity.getPrice());
    }

    /**
     * Converts a {@link ProductDTO} to {@link ProductEntity} based on the productDTO object.
     *
     * @param productDTO Provides data for conversion.
     * @return ProductEntity object based on the object of {@link ProductDTO} in the parameter.
     */
    public static ProductEntity mapToProductEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }
        return new ProductEntity(productDTO.getId(), productDTO.getName(), productDTO.getShortDescription(),
                productDTO.getFullDescription(), productDTO.getImgLink(), productDTO.getPrice());
    }

    /**
     * Converts a {@link CartEntity} to {@link CartDTO} based on the cartEntity object.
     *
     * @param cartEntity Provides data for conversion.
     * @return CartDTO object based on the object of {@link CartEntity} in the parameter.
     */
    public static CartDTO mapToCartDTO(CartEntity cartEntity) {
        if (cartEntity == null) {
            return null;
        }
        return new CartDTO(cartEntity.getId(), cartEntity.getUser().getId(), cartEntity.isCompleted());
    }

    /**
     * Converts list of {@link ProductEntity} to list of {@link ProductDTO}.
     *
     * @param productEntityList Provides data for conversion.
     * @return List of ProductDTO based on the list of {@link ProductEntity} in the parameter.
     */
    public static List<ProductDTO> mapToProductDTOList(List<ProductEntity> productEntityList) {
        if (productEntityList == null) {
            return null;
        } else {
            return productEntityList.stream().map(MappingUtils::mapToProduct).collect(Collectors.toList());
        }
    }

    /**
     * Converts list of {@link ProductDTO} to list of {@link ProductEntity}.
     *
     * @param productDTOList Provides data for conversion.
     * @return List of ProductEntity based on the list of {@link ProductDTO} in the parameter.
     */
    public static List<ProductEntity> mapToProductEntityList(List<ProductDTO> productDTOList) {
        if (productDTOList == null) {
            return null;
        } else {
            return productDTOList.stream().map(MappingUtils::mapToProductEntity).collect(Collectors.toList());
        }
    }

    /**
     * Converts {@link CartDTO} to {@link CartEntity} based on the cartDTO object.
     *
     * @param cartDTO Provides data for conversion.
     * @return CartEntity object based on the object of {@link CartDTO} in the parameter.
     */
    public static CartEntity mapToCartEntity(CartDTO cartDTO) {
        if (cartDTO == null) {
            return null;
        }
        return new CartEntity(cartDTO.getId(), new UserEntity(), cartDTO.getIsCompleted(),
                new ArrayList<CartProduct>());
    }

    /**
     * Converts a {@link CartProduct} to {@link CartProductDTO} based on the CartProduct object.
     *
     * @param cartProduct Provides data for conversion.
     * @return CartProductDTO object based on the object of {@link CartProduct} in the parameter.
     */
    public static CartProductDTO mapToCartProductDTO(CartProduct cartProduct) {
        if (cartProduct == null) {
            return null;
        }
        return new CartProductDTO(mapToProduct(cartProduct.getProduct()), cartProduct.getQuantity(),
                cartProduct.getPrice());
    }

    /**
     * Converts list of {@link CartProduct} to list of {@link CartProductDTO}.
     *
     * @param cartProductList Provides data for conversion.
     * @return List of CartProductDTO based on the list of {@link CartProduct} in the parameter.
     */
    public static List<CartProductDTO> mapToCartProductDTOList(List<CartProduct> cartProductList) {
        if (cartProductList == null) {
            return null;
        } else {
            return cartProductList.stream().map(MappingUtils::mapToCartProductDTO).collect(Collectors.toList());
        }
    }

    /**
     * Converts a {@link CartProduct} to {@link CartItemDTO}.
     *
     * @param cartProduct Provides data for conversion.
     * @return CartItemDTO object based on the object of {@link CartProduct} in the parameter.
     */
    public static CartItemDTO mapCartProductToCartItemDTO(CartProduct cartProduct) {
        if (cartProduct == null) {
            return null;
        }
        return new CartItemDTO(cartProduct.getProduct().getId(), cartProduct.getProduct().getName(),
                cartProduct.getProduct().getShortDescription(), cartProduct.getProduct().getImgLink(),
                cartProduct.getQuantity(),
                cartProduct.getPrice().multiply(BigDecimal.valueOf(cartProduct.getQuantity())));
    }

    /**
     * Converts a {@link ActiveCart} to {@link ActiveCartDTO}.
     *
     * @param activeCart Provides data for conversion.
     * @return ActiveCartDTO object based on the object of {@link ActiveCart} in the parameter.
     */
    public static ActiveCartDTO mapActiveCartToActiveCartDTO(ActiveCart activeCart) {
        if (activeCart == null) {
            return null;
        }
        return new ActiveCartDTO(activeCart.getCartId(), activeCart.getUserId(),
                activeCart.getCartItems().stream().map(MappingUtils::mapCartItemToCartItemDTO)
                        .collect(Collectors.toList()));
    }

    /**
     * Converts a {@link CartItem} to {@link CartItemDTO}.
     *
     * @param cartItem Provides data for conversion.
     * @return CartItemDTO object based on the object of {@link CartItem} in the parameter.
     */
    public static CartItemDTO mapCartItemToCartItemDTO(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        return new CartItemDTO(cartItem.getProductId(), cartItem.getName(), cartItem.getDescription(),
                cartItem.getImageUrl(), cartItem.getQuantity(), cartItem.getPriceTotal());
    }

    /**
     * Converts a {@link CartProduct} to {@link CartItem}.
     *
     * @param cartProduct Provides data for conversion.
     * @return CartItem object based on the object of {@link CartProduct} in the parameter.
     */
    public static CartItem mapCartProductToCartItem(CartProduct cartProduct) {
        if (cartProduct == null) {
            return null;
        }
        return new CartItem(cartProduct.getProduct().getId(), cartProduct.getProduct().getName(),
                cartProduct.getProduct().getShortDescription(), cartProduct.getProduct().getImgLink(),
                cartProduct.getQuantity(),
                cartProduct.getPrice().multiply(BigDecimal.valueOf(cartProduct.getQuantity())));
    }

    /**
     * Converts a {@link CartProduct} to {@link ActiveCartDTO} based on the cartProduct object.
     *
     * @param cartProduct Provides data for conversion.
     * @return ProductDTO object based on the object of {@link CartProduct} in the parameter.
     */
    public static ProductDTO mapCartProductToProductDTO(CartProduct cartProduct) {
        if (cartProduct == null) {
            return null;
        }
        return mapToProduct(cartProduct.getProduct());
    }

    /**
     * Converts {@link OrderEntity} to {@link OrderDTO} based on the orderEntity object.
     *
     * @param orderEntity Provides data for conversion.
     * @return OrderDTO object based on the object of {@link OrderEntity} in the parameter.
     */
    public static OrderDTO mapToOrderDTO(OrderEntity orderEntity) {
        if (orderEntity == null) {
            return null;
        }
        return new OrderDTO(orderEntity.getId(), orderEntity.getCustomerId());
    }

    /**
     * Converts {@link OrderDTO} to {@link OrderEntity} based on the orderDTO object.
     *
     * @param orderDTO Provides data for conversion.
     * @return OrderEntity object based on the object of {@link OrderDTO} in the parameter.
     */
    public static OrderEntity mapToOrderEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }
        return new OrderEntity(orderDTO.getId(), orderDTO.getCustomerId());
    }

    public static ProductPaginationDTO mapToProductPaginationDTO(ProductPagination productPagination) {
        if (productPagination == null) {
            return null;
        }
        ProductPaginationDTO productPaginationDTO = new ProductPaginationDTO();
        productPaginationDTO.setTotalItems(productPagination.getTotalItems());
        productPaginationDTO.setProductDTOS(mapToProductDTOList(productPagination.getProductEntities()));
        productPaginationDTO.setTotalPages(productPagination.getTotalPages());
        productPaginationDTO.setCurrentPage(productPagination.getCurrentPage());

        return productPaginationDTO;
    }
}