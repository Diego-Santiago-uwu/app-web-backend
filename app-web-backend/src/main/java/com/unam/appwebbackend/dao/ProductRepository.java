package com.unam.appwebbackend.dao;

import com.unam.appwebbackend.entity.Product;

import com.unam.appwebbackend.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "product", path = "product")

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySku(String sku);

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByCategoryCategoryName(String categoryName);

    @Query("SELECT p FROM Product p WHERE p.category.categoryName = :categoryName OR p.category.parentCategory.categoryName = :categoryName")
    List<Product> findByCategoryNameIncludingSubcategories(@Param("categoryName") String categoryName);

    Product findByproductId(Long productId);



}

