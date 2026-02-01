package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;

@Mapper(componentModel = "spring")
//Mục đích: Chuyển đổi giữa các đối tượng User và các DTO liên quan
public interface UserMapper {
    
    // Chuyển đổi từ UserCreationRequest sang User entity
    User toUser(UserCreationRequest request);

    // Chuyển đổi từ User entity sang UserResponse DTO
    UserResponse toUserResponse(User user);

    // Cập nhật thông tin User entity từ UserUpdateRequest DTO
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
