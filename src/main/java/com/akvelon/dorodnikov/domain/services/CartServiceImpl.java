package com.akvelon.dorodnikov.domain.services;

import com.akvelon.dorodnikov.domain.CartProductPK;
import com.akvelon.dorodnikov.domain.entites.ActiveCart;
import com.akvelon.dorodnikov.domain.entites.CartEntity;
import com.akvelon.dorodnikov.domain.entites.CartProduct;
import com.akvelon.dorodnikov.domain.entites.UserEntity;
import com.akvelon.dorodnikov.domain.repositories.CartProductRepository;
import com.akvelon.dorodnikov.domain.repositories.CartRepository;
import com.akvelon.dorodnikov.utils.MappingUtils;
import com.akvelon.dorodnikov.utils.exceptions.NotEmptyCartDeletionException;
import com.akvelon.dorodnikov.utils.exceptions.NotExistUserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

/**
 * Provides business logic for {@link CartEntity}.
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartProductRepository cartProductRepository;

    private final ProductService productService;

    private final UserService userService;

    @Transactional
    @Override
    public CartEntity get(int id) {
        return cartRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public CartEntity create(CartEntity cartEntity) {
        UserEntity user = userService.get(cartEntity.getUser().getId());
        if (user == null) {
            return null;
        }
        cartEntity.setUser(user);
        return cartRepository.save(cartEntity);
    }

    @Transactional
    @Override
    public CartEntity update(int id, CartEntity newCartEntity) {
        Optional<CartEntity> cartEntityOptional = cartRepository.findById(id);
        if (cartEntityOptional.isPresent()) {
            CartEntity cartEntity = cartEntityOptional.get();
            cartEntity.setUser(newCartEntity.getUser());
            cartEntity.setCompleted(newCartEntity.isCompleted());
            cartEntity.setCartProductList(newCartEntity.getCartProductList());
            return cartRepository.save(cartEntity);
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public ActiveCart getProductsInCart(int userId) {
        CartEntity cart = cartRepository.findByUserIdAndIsCompleted(userId, false);
        if (cart != null) {
            return new ActiveCart(cart.getId(), cart.getUser().getId(),
                    cart.getCartProductList().stream().map(MappingUtils::mapCartProductToCartItem)
                            .collect(Collectors.toList()));
        }
        return null;
    }

    @Transactional
    @Override
    public ActiveCart addProductToActive(int userId, int productId, int quantity) {
        if (productService.get(productId) == null) {
            return null;
        } else if (userService.get(userId) == null) {
            throw new NotExistUserException(String.valueOf(userId));
        } else {
            CartEntity cart = cartRepository.findByUserIdAndIsCompleted(userId, false);
            if (cart == null) {
                CartEntity newCart = new CartEntity();
                newCart.setUser(userService.get(userId));
                CartProduct cartProduct = new CartProduct(new CartProductPK(productId, newCart.getId()),
                        productService.get(productId), newCart, 1, productService.get(productId).getPrice());
                List<CartProduct> cartProductList = new ArrayList<>();
                cartProductList.add(cartProduct);
                newCart.setCartProductList(cartProductList);
                newCart = cartRepository.save(newCart);
                return new ActiveCart(newCart.getId(), newCart.getUser().getId(),
                        newCart.getCartProductList().stream().map(MappingUtils::mapCartProductToCartItem)
                                .collect(Collectors.toList()));
            } else {
                CartProduct cartProduct = cartProductRepository.findByCartIdAndProductId(cart.getId(), productId);
                if (cartProduct != null) {
                    if (quantity > 0) {
                        cartProduct.setQuantity(quantity);
                    } else {
                        deleteProductInActiveCart(cart, cartProduct);
                    }
                } else {
                    CartProduct newCartProduct = new CartProduct(new CartProductPK(productId, cart.getId()),
                            productService.get(productId), cart, 1, productService.get(productId).getPrice());
                    cart.getCartProductList().add(newCartProduct);
                }
            }
            cart = cartRepository.save(cart);
            return new ActiveCart(cart.getId(), cart.getUser().getId(),
                    cart.getCartProductList().stream().map(MappingUtils::mapCartProductToCartItem)
                            .collect(Collectors.toList()));
        }
    }

    @Transactional
    @Override
    public CartEntity makeOrder(int userId) {
        CartEntity cart = cartRepository.findByUserIdAndIsCompleted(userId, false);
        if (cart == null) {
            return null;
        } else if (cart.getCartProductList().isEmpty()) {
            return cart;
        } else {
            cart.setCompleted(true);
            cart = cartRepository.save(cart);
            return cart;
        }
    }

    /**
     * Deletes {@link CartProduct} from existing non-empty {@link CartEntity}.
     *
     * @param cart Active non-empty shopping cart.
     * @param cartProduct CartProduct to be deleted and which is not {@code null}.
     */
    @Transactional
    public void deleteProductInActiveCart(CartEntity cart, CartProduct cartProduct) {
        cart.getCartProductList().remove(cartProduct);
        cart = cartRepository.save(cart);
        cartProductRepository.deleteById(new CartProductPK(cartProduct.getProduct().getId(), cart.getId()));
    }

    @Override
    public boolean delete(int id) throws NotEmptyCartDeletionException {
        try {
            return deleteInTransaction(id);
        } catch (DataIntegrityViolationException e) {
            throw new NotEmptyCartDeletionException("", e.getRootCause());
        }
    }

    @Transactional
    private boolean deleteInTransaction(int id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return true;
        }
        return false;
    }
}