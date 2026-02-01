package com.example.demo.config;

import java.util.HashSet;

import org.mapstruct.ap.shaded.freemarker.ext.beans.HashAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    // Khởi tạo dữ liệu admin khi ứng dụng khởi động
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByName("admin").isEmpty()){
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());
                // Tạo user admin với mật khẩu đã mã hóa
                User user = User.builder()
                    .name("admin")
                    .password(passwordEncoder.encode("admin123")) // Mật khẩu đã được mã hóa
                    .roles(roles)
                    .build();
                // Lưu user admin vào cơ sở dữ liệu
                userRepository.save(user);
                log.warn("Admin user created with username 'admin'and password 'admin123', please change the password after first login.");
            }
        };
    }
}
