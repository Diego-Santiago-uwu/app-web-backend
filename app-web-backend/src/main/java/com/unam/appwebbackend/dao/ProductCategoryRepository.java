package com.unam.appwebbackend.dao;

import com.unam.appwebbackend.entity.Product;
import com.unam.appwebbackend.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "productCategory", path = "product-category")
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    List<ProductCategory> findByCategoryName(String categoryName);

    List<ProductCategory> findByCategoryNameAndParentCategoryCategoryName(String subCategoryName, String parentCategoryName);

    List<ProductCategory> findByParentCategoryCategoryName(String productCategories);

}
