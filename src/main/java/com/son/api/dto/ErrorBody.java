package com.son.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorBody {

    private List<CustomError> errors;

    @Data
    @Builder
    public static class CustomError {

        private String code;
        private String message;
        private String detailMessage;

    }
}