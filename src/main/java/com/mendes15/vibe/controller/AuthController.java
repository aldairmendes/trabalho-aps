package com.mendes15.vibe.controller;

import com.mendes15.vibe.dto.AuthRequest;
import com.mendes15.vibe.dto.AuthResponse;
import com.mendes15.vibe.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints públicos de autenticação.
 *
 * POST /api/auth/register  — cria conta e retorna tokens
 * POST /api/auth/login     — autentica e retorna tokens
 * POST /api/auth/refresh   — troca refresh token por novo par de tokens
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody AuthRequest.Register request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest.Login request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody AuthRequest.Refresh request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    // ── Tratamento de erros simples (use @ControllerAdvice para centralizar) ──

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}