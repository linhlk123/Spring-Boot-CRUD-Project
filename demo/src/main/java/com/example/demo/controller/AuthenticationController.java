package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.exception.ErrorCode;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .code(ErrorCode.SUCCESSFUL.getCode())
                .build();
    }
    
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
                throws JOSEException, ParseException {
        var result = authenticationService.introspect(request);
        
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .code(ErrorCode.SUCCESSFUL.getCode())
                .build();
    }
}
