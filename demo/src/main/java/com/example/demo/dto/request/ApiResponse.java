package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;



@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse<T> {
    // Mã trạng thái của phản hồi, mặc định là 200 (thành công)
    @Builder.Default
    private int code = 200;
    private String message;
    private T result;


    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }

    //getters and setters
}