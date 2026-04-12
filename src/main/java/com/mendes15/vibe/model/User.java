package com.mendes15.vibe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private String password;   // bcrypt hash

    /**
     * Roles armazenadas como strings: "ROLE_USER", "ROLE_ADMIN", etc.
     * FetchType.EAGER necessário para o Spring Security carregar junto.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = Set.of("ROLE_USER");

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Refresh token salvo para validação server-side (opcional mas mais seguro) */
    @Column(length = 512)
    private String refreshToken;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}