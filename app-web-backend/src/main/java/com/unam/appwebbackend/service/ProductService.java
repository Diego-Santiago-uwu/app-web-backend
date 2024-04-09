package com.unam.appwebbackend.service;

import com.unam.appwebbackend.dao.ProductCategoryRepository;
import com.unam.appwebbackend.dao.ProductRepository;
import com.unam.appwebbackend.entity.Product;
import com.unam.appwebbackend.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public Product getByproductId(Long productId){
        return productRepository.findByproductId(productId);
    }

    public List<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public List<Product> getProductByCategoryId(ProductCategory productCategoryId) {
        return productRepository.findByCategory(productCategoryId);
    }

    public List<Product> getProductByCategoryName(String categoryName) {
        return productRepository.findByCategoryCategoryName(categoryName);
    }

    public List<Product> findProductsByCategoryNameIncludingSubcategories(String categoryName) {
        List<ProductCategory> categories = productCategoryRepository.findByCategoryName(categoryName);
        List<ProductCategory> subCategories = categories.stream()
                .flatMap(cat -> productCategoryRepository.findByParentCategoryCategoryName(cat.getCategoryName()).stream())
                .toList();

        List<Product> products = new ArrayList<>();
        for (ProductCategory category : subCategories) {
            products.addAll(productRepository.findByCategoryCategoryName(category.getCategoryName()));
        }
        return products;
    }


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public Product save(Product existingProduct) {
        return productRepository.save(existingProduct);
    }
}