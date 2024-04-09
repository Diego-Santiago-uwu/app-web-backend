package com.unam.appwebbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "product_category")
@Data
public class ProductCategory {

    @Id
    @GeneratedValue( strategy =  GenerationType.IDENTITY)
    @Column
    private Long categoryId;

    @Column
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private ProductCategory parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private Set<ProductCategory> subCategoryName;

}
