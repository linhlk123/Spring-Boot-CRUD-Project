package com.example.demo.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.enums.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Định nghĩa các endpoint công khai không yêu cầu xác thực
    private final String[] PUBLIC_ENDPOINTS = {
            "/users",
            "/auth/token",
            "/auth/introspect"
    };
    
    //Lấy secret key từ file cấu hình application.properties
    @Value("${jwt.signer-key}")
    private String SIGNER_KEY;

    //Cấu hình PasswordEncoder để mã hóa mật khẩu người dùng
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Cấu hình các endpoint công khai và bảo vệ các endpoint khác
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request ->
            request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                    .requestMatchers(HttpMethod.GET, "/users").hasAnyAuthority(Role.ADMIN.name())
                   .anyRequest().authenticated());
        //Tạo OAuth2 Resource Server với JWT và cấu hình bộ giải mã JWT
        //Sử dụng phương thức jwtDecoder() để giải mã token JWT
        //Sử dụng Resource Server vì ứng dụng này chỉ xác thực token mà không cấp phát token
        //Ngược lại, nếu muốn cấp phát token, ta sẽ sử dụng Authorization Server. 
        //Nếu muốn vừa cấp phát vừa xác thực token, ta sẽ kết hợp cả hai.
        //Resource Server chỉ tập trung vào việc xác thực token một cách hiệu quả.
        //Authorization Server tập trung vào việc cấp phát token và quản lý quyền truy cập.
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> {
            jwtConfigurer.decoder(jwtDecoder())
            .jwtAuthenticationConverter(jwtAuthenticationConverter());
        }));
        
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    //Dùng secret key để mã hóa và giải mã JWT
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key = new SecretKeySpec(SIGNER_KEY.getBytes(), "HmacSHA512");
        //NimbusJwtDecoder sử dụng khóa bí mật để giải mã JWT
        return NimbusJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

}


//Sử dụng NimbusJwtDecoder vì nó cung cấp khả năng tùy chỉnh cao và hỗ trợ nhiều thuật toán ký khác nhau,
//bao gồm cả HMAC với SHA-512 (HS512) mà ta đang sử dụng