package com.unam.appwebbackend.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ErrorResponse {

    private int status;
    private String message;
    private long timestamp;

}