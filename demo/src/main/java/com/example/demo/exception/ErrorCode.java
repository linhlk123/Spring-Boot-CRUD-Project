package com.example.demo.exception;

public enum ErrorCode {
    // Mã lỗi và thông điệp tương ứng
    USER_EXISTED(1001, "User already existed"),
    USER_NOT_FOUND(1002, "User not found"),
    INVALID_REQUEST(1003, "Invalid request data"),
    UNCATEGORIZED_ERROR(1999, "Uncategorized error"),
    NAME_TOO_SHORT(1004, "Name must be at least 3 characters long"),
    PASSWORD_TOO_SHORT(1005, "Password must be at least 8 characters long"),
    INVALID_KEY(1006, "Invalid key provided"),
    INVALID_CREDENTIALS(1007, "Invalid credentials provided"),
    SUCCESSFUL(200, "Success");
    private int code;
    private String message;
    
    //constructor
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    //getters
    
}
