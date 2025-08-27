package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check", description = "Application health monitoring endpoints")
public class HealthController {
    
    @GetMapping
    @Operation(summary = "Health check", 
              description = "Check if the application is running and healthy")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Application is healthy",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "503", description = "Application is unhealthy")
    })
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "Spring Boot Swagger Example");
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }
    
    @GetMapping("/detailed")
    @Operation(summary = "Detailed health check", 
              description = "Get detailed health information including dependencies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detailed health information",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "Spring Boot Swagger Example");
        health.put("version", "1.0.0");
        
        // Components health
        Map<String, Object> components = new HashMap<>();
        components.put("database", Map.of("status", "UP", "type", "In-Memory"));
        components.put("disk", Map.of("status", "UP", "freeSpace", "10GB"));
        
        health.put("components", components);
        
        // System info
        Map<String, Object> system = new HashMap<>();
        system.put("java.version", System.getProperty("java.version"));
        system.put("os.name", System.getProperty("os.name"));
        system.put("processors", Runtime.getRuntime().availableProcessors());
        
        health.put("system", system);
        
        return ResponseEntity.ok(health);
    }
}