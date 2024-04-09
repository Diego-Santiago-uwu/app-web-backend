package com.unam.appwebbackend.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LoginRequest {
    private String email;
    private String password;

    // getters y setters
}