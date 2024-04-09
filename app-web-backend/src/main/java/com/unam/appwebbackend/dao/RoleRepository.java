package com.unam.appwebbackend.dao;

import com.unam.appwebbackend.entity.Role;
import com.unam.appwebbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(Role.ERole name);

}
