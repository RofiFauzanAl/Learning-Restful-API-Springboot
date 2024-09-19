package com.movieflix.movieApi.dto.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseApiDto {
    private Integer statusCode;
    private String message;
    private HttpStatus status;
    private Object data;
}
