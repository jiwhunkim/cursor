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
@Schema(description = "User entity")
public class User {
    
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Username of the user", example = "johndoe", required = true)
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address of the user", example = "john.doe@example.com", required = true)
    private String email;
    
    @NotBlank(message = "First name is required")
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "Phone number of the user", example = "+1234567890")
    private String phoneNumber;
    
    @Schema(description = "Indicates if the user account is active", example = "true")
    private boolean active = true;
}