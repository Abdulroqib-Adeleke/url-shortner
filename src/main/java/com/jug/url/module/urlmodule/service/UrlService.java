package com.jug.url.module.urlmodule.service;

import com.jug.url.dto.request.UrlDto;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.dto.response.UrlResponse;

public interface UrlService {

    ResponseWrapper<UrlResponse> generateShortUrl(UrlDto payload);
    ResponseWrapper<UrlResponse> redirectToLongUrl(String shortCode);

}
