package com.unam.appwebbackend.controller;

import com.unam.appwebbackend.dao.ProductRepository;
import com.unam.appwebbackend.dao.UserRepository;
import com.unam.appwebbackend.entity.Product;
import com.unam.appwebbackend.entity.ShoppingCart;
import com.unam.appwebbackend.entity.User;
import com.unam.appwebbackend.service.ShoppingCartService;
import com.unam.appwebbackend.utils.ShoppingCartRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class ShoppingCartController {

    @Autowired
    private final ShoppingCartService shoppingCartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public List<ShoppingCart> getAllCarts() {
        return shoppingCartService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCart> getCartById(@PathVariable Long id) {
        return shoppingCartService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ShoppingCart createCart(@RequestBody ShoppingCartRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + request.getUserId()));
        List<Product> products = productRepository.findAllById(request.getProductIds());

        ShoppingCart cart = user.getShoppingCart();
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setUser(user);
        }

        for (Product product : products) {
            if (!cart.getProducts().contains(product)) {
                cart.getProducts().add(product);
            }
        }

        return shoppingCartService.save(cart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        shoppingCartService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<ShoppingCart> createOrUpdateShoppingCart(@RequestBody ShoppingCartRequest request) {
        ShoppingCart shoppingCart = shoppingCartService.createOrUpdateShoppingCart(request.getUserId(), request.getProductIds());
        return ResponseEntity.ok(shoppingCart);
    }

    @GetMapping("/user/{userId}")
    public List<Product> getProductsInCart(@PathVariable Long userId) {
        ShoppingCart cart = shoppingCartService.getCartByUserId(userId);
        return cart.getProducts();
    }

    @DeleteMapping("/{userId}/products/{productId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        shoppingCartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/username/{username}")
    public ResponseEntity<ShoppingCart> createCartByUsername(@PathVariable String username, @RequestBody ShoppingCartRequest request) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<Product> products = productRepository.findAllById(request.getProductIds());

        ShoppingCart cart = user.getShoppingCart();
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setUser(user);
        }

        for (Product product : products) {
            if (!cart.getProducts().contains(product)) {
                cart.getProducts().add(product);
            }
        }

        return ResponseEntity.ok(shoppingCartService.save(cart));
    }
}
