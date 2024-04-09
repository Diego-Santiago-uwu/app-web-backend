package com.unam.appwebbackend.service;

import com.unam.appwebbackend.dao.ProductCategoryRepository;

import com.unam.appwebbackend.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> findSubCategoryWithinCategory(String parentCategoryName, String subCategoryName) {
        return productCategoryRepository.findByCategoryNameAndParentCategoryCategoryName(subCategoryName, parentCategoryName);
    }

    public List<ProductCategory> findCategoryName(String categoryName){
        return productCategoryRepository.findByCategoryName(categoryName);
    }


}
