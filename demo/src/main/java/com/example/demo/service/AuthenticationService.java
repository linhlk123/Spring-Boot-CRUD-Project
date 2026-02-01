package com.example.demo.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    //Repository để truy xuất dữ liệu người dùng
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    
    //Lấy secret key từ file cấu hình application.properties
    @NonFinal
    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;

    // Phương thức xác thực người dùng và cấp token JWT
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        // Verify the password
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }


    // Phương thức tạo token JWT
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(user.getName())
            .issuer("com.example.demo")
            .issueTime(new Date())
            .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())) // 1 hour expiration
            .claim("scope", buildScope(user))
            .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (Exception e) {
            System.err.println("Error signing the token: " + e.getMessage());
            throw new RuntimeException("Error signing the token", e);
        }
    }

    // Xây dựng chuỗi scope từ vai trò của người dùng
    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");

        // collectionutils để kiểm tra null hoặc rỗng
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }
    // Phương thức kiểm tra tính hợp lệ của token JWTs
    public IntrospectResponse introspect(IntrospectRequest request) 
            throws JOSEException, ParseException {
        var token = request.getToken();
        JWSVerifier verifier =  new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        //kiểm tra hết hạn token
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expirationTime.after(new Date())) //boolean
                .build();
    
    }
}

