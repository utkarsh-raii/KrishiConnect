package com.krishiconnect.service;

import com.krishiconnect.dto.*;

public interface AuthService {
    AuthResponse register(RegisterRequest req);
    AuthResponse login(LoginRequest req);
    UserDto getMe(Long userId);
    UserDto updateProfile(Long userId, UpdateProfileRequest req);
    void changePassword(Long userId, ChangePasswordRequest req);
    void bootstrapAdmin();
}
