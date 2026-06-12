package com.jug.url.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
public class UrlDto {

    @URL(message = "please provide a valid url")
    private String url;

}
