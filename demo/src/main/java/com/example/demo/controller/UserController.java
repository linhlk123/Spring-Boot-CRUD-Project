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
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserCreationRequest request){
        return userService.createRequest(request);
    }
    
    @GetMapping
    List<User> getAllUsers() {
        return (List<User>) userService.getAllUsers();
    }

    @GetMapping("/{id}")
    User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }
    
    @PutMapping("/{id}")
    User updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request){
        return userService.updateUser(id, request);
    } 

    @DeleteMapping("/{id}")
    String deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return "User with id " + id + " has been deleted.";
    }
}
