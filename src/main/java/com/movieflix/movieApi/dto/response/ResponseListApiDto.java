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
public class ResponseListApiDto {
    private Integer statusCode;
    private String message;
    private HttpStatus status;
    private Long totalData;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;
    private Object data;
}
