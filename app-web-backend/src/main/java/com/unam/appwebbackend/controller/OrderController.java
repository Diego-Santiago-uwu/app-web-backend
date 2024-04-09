package com.unam.appwebbackend.controller;

import com.unam.appwebbackend.dao.UserRepository;
import com.unam.appwebbackend.entity.Order;
import com.unam.appwebbackend.entity.User;
import com.unam.appwebbackend.service.OrderService;
import com.unam.appwebbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.save(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.ok().build();
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
}