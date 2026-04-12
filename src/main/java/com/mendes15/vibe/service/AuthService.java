package com.mendes15.vibe.service;

import com.mendes15.vibe.dto.AuthRequest;
import com.mendes15.vibe.dto.AuthResponse;
import com.mendes15.vibe.model.User;
import com.mendes15.vibe.repository.UserRepository;
import com.mendes15.vibe.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository     userRepository;
    private final PasswordEncoder    passwordEncoder;
    private final JwtUtil            jwtUtil;
    private final AuthenticationManager authManager;

    // ── Registro ──────────────────────────────────────────────────────────

    @Transactional
    public AuthResponse register(AuthRequest.Register req) {
        if (userRepository.existsByUsername(req.username()))
            throw new IllegalArgumentException("Username já em uso.");
        if (userRepository.existsByEmail(req.email()))
            throw new IllegalArgumentException("Email já cadastrado.");

        var user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRoles(Set.of("ROLE_USER"));

        String refresh = jwtUtil.generateRefreshToken(req.username());
        user.setRefreshToken(refresh);
        userRepository.save(user);

        String access = jwtUtil.generateAccessToken(req.username(), user.getRoles());
        return AuthResponse.of(access, refresh, jwtUtil.getExpirationMs(), req.username(),
                List.copyOf(user.getRoles()));
    }

    // ── Login ─────────────────────────────────────────────────────────────

    @Transactional
    public AuthResponse login(AuthRequest.Login req) {
        // Lança BadCredentialsException se inválido
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));

        var user = userRepository.findByUsername(req.username()).orElseThrow();

        String access  = jwtUtil.generateAccessToken(req.username(), user.getRoles());
        String refresh = jwtUtil.generateRefreshToken(req.username());

        // Atualiza refresh token no banco (rotação a cada login)
        user.setRefreshToken(refresh);
        userRepository.save(user);

        return AuthResponse.of(access, refresh, jwtUtil.getExpirationMs(), req.username(),
                List.copyOf(user.getRoles()));
    }

    // ── Refresh ───────────────────────────────────────────────────────────

    @Transactional
    public AuthResponse refresh(AuthRequest.Refresh req) {
        String token = req.refreshToken();

        if (!jwtUtil.isValidRefresh(token))
            throw new IllegalArgumentException("Refresh token inválido ou expirado.");

        // Valida que o token bate com o salvo no banco (proteção contra reutilização)
        var user = userRepository.findByRefreshToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token não reconhecido."));

        String newAccess  = jwtUtil.generateAccessToken(user.getUsername(), user.getRoles());
        String newRefresh = jwtUtil.generateRefreshToken(user.getUsername());

        // Rotação: invalida o token antigo
        user.setRefreshToken(newRefresh);
        userRepository.save(user);

        return AuthResponse.of(newAccess, newRefresh, jwtUtil.getExpirationMs(), user.getUsername(),
                List.copyOf(user.getRoles()));
    }
}