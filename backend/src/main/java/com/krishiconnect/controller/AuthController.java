package com.krishiconnect.controller;

import com.krishiconnect.dto.*;
import com.krishiconnect.security.UserPrincipal;
import com.krishiconnect.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest req) {
        AuthResponse auth = authService.register(req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Registration successful");
        res.put("token", auth.getToken());
        res.put("user", auth.getUser());
        res.put("data", auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest req) {
        AuthResponse auth = authService.login(req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Login successful");
        res.put("token", auth.getToken());
        res.put("user", auth.getUser());
        res.put("data", auth);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        UserDto user = authService.getMe(currentUser.getId());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("user", user);
        res.put("data", user);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(@AuthenticationPrincipal UserPrincipal currentUser,
                                                             @RequestBody UpdateProfileRequest req) {
        UserDto user = authService.updateProfile(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Profile updated successfully");
        res.put("user", user);
        res.put("data", user);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@AuthenticationPrincipal UserPrincipal currentUser,
                                                              @Valid @RequestBody ChangePasswordRequest req) {
        authService.changePassword(currentUser.getId(), req);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Password changed successfully");
        return ResponseEntity.ok(res);
    }
}
