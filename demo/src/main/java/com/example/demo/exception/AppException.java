package com.example.demo.exception;

public class AppException extends RuntimeException {
    private final ErrorCode errorCode;

    // Constructor khởi tạo ngoại lệ với mã lỗi cụ thể
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    // Phương thức lấy mã lỗi
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}