package by.karpovich.security.service;

import by.karpovich.security.web.dto.auth.JwtRequest;
import by.karpovich.security.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);
    JwtResponse refresh(String refreshToken);
}
