package com.mendes15.vibe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// ── Registro ──────────────────────────────────────────────────────────────────
public sealed interface AuthRequest permits
        AuthRequest.Register,
        AuthRequest.Login,
        AuthRequest.Refresh {

    record Register(
            @NotBlank @Size(min = 3, max = 50)  String username,
            @NotBlank @Email                     String email,
            @NotBlank @Size(min = 8, max = 100)  String password
    ) implements AuthRequest {}

    record Login(
            @NotBlank String username,
            @NotBlank String password
    ) implements AuthRequest {}

    record Refresh(
            @NotBlank String refreshToken
    ) implements AuthRequest {}
}