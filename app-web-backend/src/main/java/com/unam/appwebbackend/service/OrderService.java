package com.unam.appwebbackend.service;

import com.unam.appwebbackend.dao.OrderRepository;
import com.unam.appwebbackend.dao.ShoppingCartRepository;
import com.unam.appwebbackend.dao.UserRepository;
import com.unam.appwebbackend.entity.Order;
import com.unam.appwebbackend.entity.ShoppingCart;
import com.unam.appwebbackend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private View error;

    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    public Order createOrderFromShoppingCart(User user) {
        ShoppingCart shoppingCart = user.getShoppingCart();

        Order order = new Order();
        order.setUser(user);
        order.setShoppingCart(shoppingCart);
        order.setStatus(Order.OrderStatus.PENDING);

        // Set the shipping address
        if (!user.getAddresses().isEmpty()) {
            order.setShippingAddress(user.getAddresses().get(0));
        }

        orderRepository.save(order);

        // Create a new shopping cart for the user
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(user);
        user.setShoppingCart(newShoppingCart);
        shoppingCartRepository.save(newShoppingCart);

        return order;
    }

    public List<Order> findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return orderRepository.findByUser(user);
    }

    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }
}