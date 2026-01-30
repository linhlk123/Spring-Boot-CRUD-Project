package com.example.demo.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserCreationRequest {
    @Size(min = 3, message = "NAME_TOO_SHORT")
    String name;

    @Size(min = 8, message = "PASSWORD_TOO_SHORT")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;

}
   