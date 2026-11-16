package com.krishiconnect.service.impl;

import com.krishiconnect.dto.*;
import com.krishiconnect.entity.User;
import com.krishiconnect.exception.BadRequestException;
import com.krishiconnect.exception.ResourceNotFoundException;
import com.krishiconnect.exception.UnauthorizedException;
import com.krishiconnect.repository.UserRepository;
import com.krishiconnect.security.JwtTokenProvider;
import com.krishiconnect.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService, CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Value("${app.admin.name:FHC Admin}")
    private String adminName;

    @Value("${app.admin.email:admin@fhc.com}")
    private String adminEmail;

    @Value("${app.admin.password:Admin@1234}")
    private String adminPassword;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if ("admin".equalsIgnoreCase(req.getRole())) {
            throw new BadRequestException("Admin accounts cannot be self-registered");
        }

        String email = req.getEmail().toLowerCase().trim();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("An account with this email already exists");
        }

        if (req.getPhone() != null && !req.getPhone().isBlank() && userRepository.existsByPhone(req.getPhone().trim())) {
            throw new BadRequestException("An account with this phone number already exists");
        }

        User user = new User();
        user.setName(req.getName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setPhone(req.getPhone() != null ? req.getPhone().trim() : null);
        user.setRole(req.getRole().toLowerCase().trim());
        user.setIsVerified(false);
        user.setIsBlocked(false);

        if (req.getProfile() != null) {
            user.setLocation(req.getProfile().getLocation());
            user.setBusinessName(req.getProfile().getBusinessName());
            user.setServiceType(req.getProfile().getServiceType());
            user.setVehicleCount(req.getProfile().getVehicleCount());
            user.setBio(req.getProfile().getBio());
            user.setAvatar(req.getProfile().getAvatar());
        }

        user = userRepository.save(user);

        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token, UserDto.fromEntity(user));
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest req) {
        String email = req.getEmail().toLowerCase().trim();
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        if (req.getRole() != null && !req.getRole().isBlank()) {
            if (!req.getRole().equalsIgnoreCase(user.getRole())) {
                throw new UnauthorizedException("This account is not registered as " + req.getRole());
            }
        }

        if (Boolean.TRUE.equals(user.getIsBlocked())) {
            throw new UnauthorizedException("Your account has been blocked. Please contact support.");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token, UserDto.fromEntity(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserDto.fromEntity(user);
    }

    @Override
    @Transactional
    public UserDto updateProfile(Long userId, UpdateProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (req.getName() != null && !req.getName().isBlank()) {
            user.setName(req.getName().trim());
        }
        if (req.getPhone() != null && !req.getPhone().isBlank()) {
            user.setPhone(req.getPhone().trim());
        }
        if (req.getProfile() != null) {
            if (req.getProfile().getLocation() != null) user.setLocation(req.getProfile().getLocation());
            if (req.getProfile().getBusinessName() != null) user.setBusinessName(req.getProfile().getBusinessName());
            if (req.getProfile().getServiceType() != null) user.setServiceType(req.getProfile().getServiceType());
            if (req.getProfile().getVehicleCount() != null) user.setVehicleCount(req.getProfile().getVehicleCount());
            if (req.getProfile().getBio() != null) user.setBio(req.getProfile().getBio());
            if (req.getProfile().getAvatar() != null) user.setAvatar(req.getProfile().getAvatar());
        }

        user = userRepository.save(user);
        return UserDto.fromEntity(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void bootstrapAdmin() {
        if (adminEmail == null || adminEmail.isBlank()) return;
        String normalizedEmail = adminEmail.toLowerCase().trim();
        if (!userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            logger.info("🚀 Bootstrapping admin user: {}", normalizedEmail);
            User admin = new User();
            admin.setName(adminName != null ? adminName : "FHC Admin");
            admin.setEmail(normalizedEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole("admin");
            admin.setPhone("9000000001");
            admin.setIsVerified(true);
            admin.setIsBlocked(false);
            userRepository.save(admin);
            logger.info("✅ Admin user bootstrapped successfully");
        }
    }

    @Override
    public void run(String... args) {
        bootstrapAdmin();
    }
}
