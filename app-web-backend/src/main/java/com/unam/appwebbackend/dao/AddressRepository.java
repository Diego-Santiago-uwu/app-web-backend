package com.unam.appwebbackend.dao;

import com.unam.appwebbackend.entity.Address;
import com.unam.appwebbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "Address", path = "Address")
public interface AddressRepository extends JpaRepository<Address, Long> {
}