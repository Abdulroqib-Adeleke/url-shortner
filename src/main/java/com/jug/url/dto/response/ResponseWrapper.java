package com.jug.url.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatusCode;

@Builder
public class ResponseWrapper <T>{
    private T data;
    private String message;
    private HttpStatusCode statusCode;
}
