package com.jug.url.dto.response;

import org.springframework.http.HttpStatusCode;

public class ResponseWrapper <T>{
    private T data;
    private String message;
    private HttpStatusCode code;
}
