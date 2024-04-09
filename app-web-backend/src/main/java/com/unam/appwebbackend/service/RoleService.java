package com.unam.appwebbackend.service;

import com.unam.appwebbackend.dao.RoleRepository;
import com.unam.appwebbackend.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(Role.ERole name) {
        return roleRepository.findByName(name);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}