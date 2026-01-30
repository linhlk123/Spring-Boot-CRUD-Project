package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.dto.request.ApiResponse;




@ControllerAdvice

public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception ex) {
        ApiResponse apiResponse = ApiResponse.builder()
                                            .code(ErrorCode.UNCATEGORIZED_ERROR.getCode())
                                            .message(ErrorCode.UNCATEGORIZED_ERROR.getMessage())
                                            .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }
 
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse apiResponse = ApiResponse.builder()
                                            .code(errorCode.getCode())
                                            .message(errorCode.getMessage())
                                            .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidationException(MethodArgumentNotValidException ex) {
        String enumKey = ex.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            errorCode = ErrorCode.UNCATEGORIZED_ERROR;
        }
        ApiResponse apiResponse = ApiResponse.builder()
                                            .code(errorCode.getCode())
                                            .message(errorCode.getMessage())
                                            .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
