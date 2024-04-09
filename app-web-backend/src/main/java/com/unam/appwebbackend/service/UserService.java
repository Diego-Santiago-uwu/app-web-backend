package com.unam.appwebbackend.service;

import com.unam.appwebbackend.dao.AddressRepository;
import com.unam.appwebbackend.dao.RoleRepository;
import com.unam.appwebbackend.dao.ShoppingCartRepository;
import com.unam.appwebbackend.dao.UserRepository;
import com.unam.appwebbackend.entity.Address;
import com.unam.appwebbackend.entity.Role;
import com.unam.appwebbackend.entity.ShoppingCart;
import com.unam.appwebbackend.entity.User;
import com.unam.appwebbackend.utils.PasswordUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private AddressRepository addressRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerNewUser(User user, Address address) {
        // Check if the email is already in use
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            // Throw an exception or return an error message
            throw new IllegalArgumentException("Email is already in use");
        }

        String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        // Create a new ShoppingCart for the user
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        user.setShoppingCart(shoppingCart);

        // Assign the address to the user and save it in the database
        address.setUser(user);
        user.getAddresses().add(address);

        // Save the User first
        userRepository.save(user);

        // Now you can save the ShoppingCart and Address
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);

        address.setUser(user);
        addressRepository.save(address);

        return user;
    }

    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // User not found
            return false;
        }
        // Check if the hashed password matches the stored hash
        return PasswordUtils.checkPassword(password, user.getPassword());
    }


    public User createAdminUser() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(PasswordUtils.hashPassword("admin"));
        admin.setEmail("admin@example.com");
        Role adminRole = roleRepository.findByName(com.unam.appwebbackend.entity.Role.ERole.ADMIN);
        admin.setRoles(Set.of(adminRole));
        return userRepository.save(admin);
    }

    public String findUsernameByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user != null ? user.getUsername() : null;
    }

    public User addAddressToUser(String username, Address address) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        for (Address existingAddress : user.getAddresses()) {
            if (existingAddress.equals(address)) {
                // La dirección ya existe, por lo que no la agregamos
                return user;
            }
        }
        address.setUser(user);
        user.getAddresses().add(address);
        return userRepository.save(user);
    }

    public User addAddressToUserByUsername(String username, Address address) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        for (Address existingAddress : user.getAddresses()) {
            if (existingAddress.equals(address)) {
                // La dirección ya existe, por lo que no la agregamos
                return user;
            }
        }
        address.setUser(user);
        user.getAddresses().add(address);
        return userRepository.save(user);
    }

}


