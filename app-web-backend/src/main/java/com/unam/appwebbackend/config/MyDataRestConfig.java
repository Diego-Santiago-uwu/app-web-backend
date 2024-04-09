package com.unam.appwebbackend.config;

import com.unam.appwebbackend.entity.Product;
import com.unam.appwebbackend.entity.ProductCategory;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Data
@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};

//
//        //Disable HTTP methods for Product: PUT, POST and DELETE
//        config.getExposureConfiguration()
//                .forDomainType(Product.class)
//                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
//                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
//
//        //Disable HTTP methods for Product - Category: PUT, POST and DELETE
//        config.getExposureConfiguration()
//                .forDomainType(ProductCategory.class)
//                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
//                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        config.exposeIdsFor(Product.class);


    }
}
