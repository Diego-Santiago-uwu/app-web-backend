package com.unam.appwebbackend.controller;

import com.unam.appwebbackend.entity.Address;
import com.unam.appwebbackend.entity.Order;
import com.unam.appwebbackend.entity.ShoppingCart;
import com.unam.appwebbackend.service.OrderService;
import com.unam.appwebbackend.service.ShoppingCartService;
import com.unam.appwebbackend.utils.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.unam.appwebbackend.entity.User;
import com.unam.appwebbackend.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderService orderRepository;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            // User with the same email already exists
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        if (user.getAddresses().isEmpty()) {
            // No address provided
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(userService.registerNewUser(user, user.getAddresses().get(0)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setRoles(user.getRoles());
                    return ResponseEntity.ok(userService.save(existingUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{userId}/cart/products/{productId}")
    public ResponseEntity<Void> addProductToCart(@PathVariable Long userId, @PathVariable Long productId) {
        shoppingCartService.addProductToCart(userId, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

        System.out.println("isAuthenticated: " + isAuthenticated);
        System.out.println("loginRequest.getEmail(): " + loginRequest.getEmail());
        System.out.println("loginRequest.getPassword(): " + loginRequest.getPassword());

        if (isAuthenticated) {
            // Aquí deberías generar y devolver un token de autenticación.
            // Este es un ejemplo simplificado y en un caso real deberías generar un token JWT o similar.
            String token = "dummyToken";
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/username")
    public ResponseEntity<String> getUsernameByEmail(@RequestParam String email) {
        String username = userService.findUsernameByEmail(email);
        if (username != null) {
            return ResponseEntity.ok(username);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/cart")
    public ResponseEntity<ShoppingCart> getCartByUserId(@PathVariable Long userId) {
        ShoppingCart cart = shoppingCartService.getCartByUserId(userId);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}/cart")
    public ResponseEntity<ShoppingCart> getCartByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        ShoppingCart cart = user.getShoppingCart();
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/username/{username}/addresses")
    public ResponseEntity<List<Address>> getAddressesByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<Address> addresses = user.getAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PostMapping("/username/{username}/addresses")
    public ResponseEntity<User> createAddressByUsername(@PathVariable String username, @RequestBody Address address) {
        User user = userService.addAddressToUserByUsername(username, address);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{username}/orders")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<Order> orders = orderService.findByUser(user);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

//    @PostMapping("/{username}/createOrderFromCart")
//    public ResponseEntity<Order> createOrderFromCart(@PathVariable String  username) {
//        User user = userService.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//        Order order = orderService.createOrderFromShoppingCart(username);
//        return ResponseEntity.ok(order);
//    }

    @PostMapping("/{username}/createOrderAndClearCart")
    public ResponseEntity<Order> createOrderAndClearCart(@PathVariable String username) throws Exception {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Create the order from the shopping cart
        Order order = orderService.createOrderFromShoppingCart(user);

        // Clear the shopping cart
        ShoppingCart cart = user.getShoppingCart();
        if (cart.getProducts() == null) {
            cart.setProducts(new ArrayList<>());
        } else {
            cart.getProducts().clear();
        }
        shoppingCartService.save(cart);

        return ResponseEntity.ok(order);
    }
}