package com.unam.appwebbackend.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ShoppingCartRequest {

    private Long userId;
    private List<Long> productIds;

}
