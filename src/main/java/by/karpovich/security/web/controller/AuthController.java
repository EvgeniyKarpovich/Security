package by.karpovich.security.web.controller;

import by.karpovich.security.service.impl.AuthServiceImpl;
import by.karpovich.security.service.impl.UserServiceImpl;
import by.karpovich.security.web.dto.auth.JwtRequest;
import by.karpovich.security.web.dto.auth.JwtResponse;
import by.karpovich.security.web.dto.user.UserDto;
import by.karpovich.security.web.validation.OnCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "Auth API")
public class AuthController {

    private final AuthServiceImpl authService;
    private final UserServiceImpl userService;

    @PostMapping("/login")
    @Operation(summary = "Login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    @Operation(summary = "Registration")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }

}

