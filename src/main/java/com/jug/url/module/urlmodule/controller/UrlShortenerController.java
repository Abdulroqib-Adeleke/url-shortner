package com.jug.url.module.urlmodule.controller;

import com.jug.url.dto.request.UrlDto;
import com.jug.url.dto.response.ResponseWrapper;
import com.jug.url.dto.response.UrlResponse;
import com.jug.url.exceptions.BadRequestException;
import com.jug.url.module.urlmodule.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlService urlService;

    @PostMapping("/api/module/urlshortener/generate-shorturl")
    ResponseWrapper<UrlResponse> generateShortUrl(@RequestBody @Valid UrlDto payload){
        return urlService.generateShortUrl(payload);
    }

    @GetMapping("/{shortCode}")
    ResponseEntity<Void> redirectToLongUrl(@PathVariable("shortCode") String shortCode)
            throws URISyntaxException {

        ResponseWrapper<UrlResponse> responseWrapper = urlService.redirectToLongUrl(shortCode);
        UrlResponse response = responseWrapper.getData();
        if (response == null) throw  new BadRequestException("No url resource found!");
        String longUrl = response.getUrl();
        if (longUrl == null) throw  new BadRequestException("No url resource found!");
        HttpHeaders headers = new HttpHeaders();
        URI uri = new URI(longUrl);
        headers.setLocation(uri);
        return  new ResponseEntity<>(headers, HttpStatus.FOUND);

    }

}
