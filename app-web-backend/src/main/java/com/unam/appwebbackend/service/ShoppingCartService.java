package com.unam.appwebbackend.service;

import com.unam.appwebbackend.dao.ProductRepository;
import com.unam.appwebbackend.dao.ShoppingCartRepository;
import com.unam.appwebbackend.dao.UserRepository;
import com.unam.appwebbackend.entity.Product;
import com.unam.appwebbackend.entity.ShoppingCart;
import com.unam.appwebbackend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public ShoppingCart findByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId);
    }

    public List<ShoppingCart> findAll() {
        return shoppingCartRepository.findAll();
    }

    public Optional<ShoppingCart> findById(Long id) {
        return shoppingCartRepository.findById(id);
    }

    public ShoppingCart save(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    public void deleteById(Long id) {
        shoppingCartRepository.deleteById(id);
    }

    public void addProductToCart(Long userId, Long productId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        shoppingCart.addProduct(product);
        shoppingCartRepository.save(shoppingCart);
    }

    public ShoppingCart createOrUpdateShoppingCart(Long userId, List<Long> productIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        ShoppingCart shoppingCart = user.getShoppingCart();
        if (shoppingCart == null) {
            throw new RuntimeException("Shopping cart not found for user Id:" + userId);
        }

        for (Long productId : productIds) {
            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
            shoppingCart.addProduct(product);
        }

        shoppingCartRepository.save(shoppingCart);
        userRepository.save(user);

        return shoppingCart;
    }

    public ShoppingCart getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        return user.getShoppingCart();
    }

    public void removeProductFromCart(Long userId, Long productId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        shoppingCart.getProducts().remove(product);
        shoppingCartRepository.save(shoppingCart);
    }

    public void clearCart(String userName) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserUsername(userName);
        shoppingCart.getProducts().clear();
        shoppingCartRepository.save(shoppingCart);
    }
}