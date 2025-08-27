package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for updating an existing user")
public class UpdateUserRequest {
    
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Updated username", example = "johndoe_updated")
    private String username;
    
    @Email(message = "Email should be valid")
    @Schema(description = "Updated email address", example = "john.doe.updated@example.com")
    private String email;
    
    @Schema(description = "Updated first name", example = "John")
    private String firstName;
    
    @Schema(description = "Updated last name", example = "Doe")
    private String lastName;
    
    @Schema(description = "Updated phone number", example = "+1234567890")
    private String phoneNumber;
    
    @Schema(description = "Updated active status", example = "true")
    private Boolean active;
}