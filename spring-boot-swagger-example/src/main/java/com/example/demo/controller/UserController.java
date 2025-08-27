package com.example.demo.controller;

import com.example.demo.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs for managing users")
@Validated
@Slf4j
public class UserController {
    
    // Simple in-memory storage for demo purposes
    private final List<User> users = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();
    
    public UserController() {
        // Initialize with some sample data
        users.add(new User(idCounter.incrementAndGet(), "admin", "admin@example.com", 
                          "Admin", "User", "+1234567890", true));
        users.add(new User(idCounter.incrementAndGet(), "johndoe", "john.doe@example.com", 
                          "John", "Doe", "+0987654321", true));
    }
    
    @GetMapping
    @Operation(summary = "Get all users", 
              description = "Retrieve a paginated list of all users in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PagedResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<com.example.demo.model.ApiResponse<PagedResponse<User>>> getAllUsers(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        
        log.info("Getting all users - page: {}, size: {}", page, size);
        
        // Simple pagination logic
        int start = page * size;
        int end = Math.min(start + size, users.size());
        
        List<User> pageContent = start < users.size() ? 
            users.subList(start, end) : new ArrayList<>();
        
        PagedResponse<User> pagedResponse = new PagedResponse<>(
            pageContent,
            page,
            size,
            users.size(),
            (int) Math.ceil((double) users.size() / size),
            end >= users.size(),
            page == 0
        );
        
        return ResponseEntity.ok(com.example.demo.model.ApiResponse.success(
            "Users retrieved successfully", pagedResponse));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", 
              description = "Retrieve a specific user by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = com.example.demo.model.ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid ID format")
    })
    public ResponseEntity<com.example.demo.model.ApiResponse<User>> getUserById(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id) {
        
        log.info("Getting user by id: {}", id);
        
        Optional<User> user = users.stream()
            .filter(u -> u.getId().equals(id))
            .findFirst();
        
        if (user.isPresent()) {
            return ResponseEntity.ok(com.example.demo.model.ApiResponse.success(
                "User found", user.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(com.example.demo.model.ApiResponse.error(
                    "User not found with id: " + id));
        }
    }
    
    @PostMapping
    @Operation(summary = "Create a new user", 
              description = "Create a new user with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = com.example.demo.model.ApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<com.example.demo.model.ApiResponse<User>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        
        log.info("Creating new user: {}", request.getUsername());
        
        // Check if user already exists
        boolean userExists = users.stream()
            .anyMatch(u -> u.getUsername().equals(request.getUsername()) || 
                          u.getEmail().equals(request.getEmail()));
        
        if (userExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(com.example.demo.model.ApiResponse.error(
                    "User with this username or email already exists"));
        }
        
        // Create new user (password would be hashed in real application)
        User newUser = new User(
            idCounter.incrementAndGet(),
            request.getUsername(),
            request.getEmail(),
            request.getFirstName(),
            request.getLastName(),
            request.getPhoneNumber(),
            true
        );
        
        users.add(newUser);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(com.example.demo.model.ApiResponse.success(
                "User created successfully", newUser));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update user", 
              description = "Update an existing user's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = com.example.demo.model.ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<com.example.demo.model.ApiResponse<User>> updateUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        
        log.info("Updating user with id: {}", id);
        
        Optional<User> existingUser = users.stream()
            .filter(u -> u.getId().equals(id))
            .findFirst();
        
        if (!existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(com.example.demo.model.ApiResponse.error(
                    "User not found with id: " + id));
        }
        
        User user = existingUser.get();
        
        // Update fields if provided
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }
        
        return ResponseEntity.ok(com.example.demo.model.ApiResponse.success(
            "User updated successfully", user));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", 
              description = "Delete a user from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = com.example.demo.model.ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<com.example.demo.model.ApiResponse<Void>> deleteUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id) {
        
        log.info("Deleting user with id: {}", id);
        
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        
        if (removed) {
            return ResponseEntity.ok(com.example.demo.model.ApiResponse.success(
                "User deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(com.example.demo.model.ApiResponse.error(
                    "User not found with id: " + id));
        }
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search users", 
              description = "Search users by username or email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results returned",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = com.example.demo.model.ApiResponse.class)))
    })
    public ResponseEntity<com.example.demo.model.ApiResponse<List<User>>> searchUsers(
            @Parameter(description = "Search query", required = true, example = "john")
            @RequestParam String query) {
        
        log.info("Searching users with query: {}", query);
        
        String lowerQuery = query.toLowerCase();
        List<User> results = users.stream()
            .filter(u -> u.getUsername().toLowerCase().contains(lowerQuery) ||
                        u.getEmail().toLowerCase().contains(lowerQuery) ||
                        u.getFirstName().toLowerCase().contains(lowerQuery) ||
                        u.getLastName().toLowerCase().contains(lowerQuery))
            .toList();
        
        return ResponseEntity.ok(com.example.demo.model.ApiResponse.success(
            "Search completed", results));
    }
}