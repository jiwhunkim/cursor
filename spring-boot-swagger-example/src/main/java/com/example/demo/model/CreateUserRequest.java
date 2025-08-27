package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating a new user")
public class CreateUserRequest {
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Username for the new user", example = "johndoe", required = true)
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address for the new user", example = "john.doe@example.com", required = true)
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Schema(description = "Password for the new user", example = "securePassword123!", required = true)
    private String password;
    
    @NotBlank(message = "First name is required")
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "Phone number of the user", example = "+1234567890")
    private String phoneNumber;
}