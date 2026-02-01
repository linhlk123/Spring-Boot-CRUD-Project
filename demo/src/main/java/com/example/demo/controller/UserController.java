package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    private UserService userService;

    // Endpoint để tạo người dùng mới
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }
    
    // Endpoint để lấy danh sách tất cả người dùng
    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers() {
        // Log thong tin người dùng đã xác thực
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        
        log.info("Authenticated user: {}", authentication.getName());
        authentication.getAuthorities().forEach(authority -> 
            log.info("Authority: {}", authority.getAuthority())
        );

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    // Endpoint để lấy thông tin người dùng theo ID
    @GetMapping("/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userId))
                .build();
    }

    // Endpoint để cập nhật thông tin người dùng
    @PutMapping("/{id}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    // Endpoint để xóa người dùng theo ID
    @DeleteMapping("/{id}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }
}
