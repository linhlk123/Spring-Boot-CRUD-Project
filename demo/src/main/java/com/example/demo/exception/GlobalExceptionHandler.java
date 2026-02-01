package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.dto.request.ApiResponse;




@ControllerAdvice

// Xử lý ngoại lệ toàn cục cho ứng dụng
public class GlobalExceptionHandler {
    // Xử lý ngoại lệ chung
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception ex) {
        ApiResponse apiResponse = ApiResponse.builder()
                                            .code(ErrorCode.UNCATEGORIZED_ERROR.getCode())
                                            .message(ErrorCode.UNCATEGORIZED_ERROR.getMessage())
                                            .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // Xử lý ngoại lệ ứng dụng tùy chỉnh
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse apiResponse = ApiResponse.builder()
                                            .code(errorCode.getCode())
                                            .message(errorCode.getMessage())
                                            .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // Xử lý ngoại lệ xác thực dữ liệu đầu vào
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidationException(MethodArgumentNotValidException ex) {
        String enumKey = ex.getFieldError().getDefaultMessage();

        // Lấy mã lỗi từ enum dựa trên khóa
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            errorCode = ErrorCode.UNCATEGORIZED_ERROR;
        }
        // Tạo phản hồi API với mã lỗi và thông điệp tương ứng
        ApiResponse apiResponse = ApiResponse.builder()
                                            .code(errorCode.getCode())
                                            .message(errorCode.getMessage())
                                            .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
