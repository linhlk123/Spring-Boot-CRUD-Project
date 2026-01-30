package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.demo.mapper.UserMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    private UserService userService;

    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<User> apiResponse = ApiResponse.<User>builder().build();
        User createdUser = userService.createRequest(request);
        apiResponse.setMessage("User created successfully");
        apiResponse.setResult(createdUser);
        return apiResponse;
    }
    
    @GetMapping
    List<User> getAllUsers() {
        return (List<User>) userService.getAllUsers();
    }

    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }
    
    @PutMapping("/{id}")
    UserResponse updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request){
        return userService.updateUser(id, request);
    } 

    @DeleteMapping("/{id}")
    String deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return "User with id " + id + " has been deleted.";
    }
}
