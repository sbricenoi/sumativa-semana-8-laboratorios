package com.duoc.laboratorio.usuarios.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para JwtUtil
 */
@DisplayName("JwtUtil Tests")
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;
    private String testEmail = "test@example.com";
    private String testRol = "PACIENTE";
    private Long testUserId = 1L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        
        // Configurar valores de prueba usando reflection
        ReflectionTestUtils.setField(jwtUtil, "secret", 
            "LaboratorioSecretKeyForJWTTokenGenerationAndValidation2025MustBeLongEnoughForHS512Algorithm");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L); // 24 horas

        // UserDetails de prueba
        userDetails = User.builder()
                .username(testEmail)
                .password("password")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_PACIENTE")))
                .build();
    }

    @Test
    @DisplayName("Generar token JWT con email, rol y userId")
    void testGenerateToken_WithEmailRolUserId() {
        // Act
        String token = jwtUtil.generateToken(testEmail, testRol, testUserId);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT tiene 3 partes separadas por puntos
    }

    @Test
    @DisplayName("Generar token JWT con UserDetails")
    void testGenerateToken_WithUserDetails() {
        // Act
        String token = jwtUtil.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Extraer username del token")
    void testExtractUsername() {
        // Arrange
        String token = jwtUtil.generateToken(testEmail, testRol, testUserId);

        // Act
        String extractedUsername = jwtUtil.extractUsername(token);

        // Assert
        assertEquals(testEmail, extractedUsername);
    }

    @Test
    @DisplayName("Extraer rol del token")
    void testExtractRole() {
        // Arrange
        String token = jwtUtil.generateToken(testEmail, testRol, testUserId);

        // Act
        String extractedRole = jwtUtil.extractRole(token);

        // Assert
        assertEquals(testRol, extractedRole);
    }

    @Test
    @DisplayName("Extraer userId del token")
    void testExtractUserId() {
        // Arrange
        String token = jwtUtil.generateToken(testEmail, testRol, testUserId);

        // Act
        Long extractedUserId = jwtUtil.extractUserId(token);

        // Assert
        assertEquals(testUserId, extractedUserId);
    }

    @Test
    @DisplayName("Validar token con UserDetails")
    void testValidateToken_WithUserDetails() {
        // Arrange
        String token = jwtUtil.generateToken(userDetails);

        // Act
        Boolean isValid = jwtUtil.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Validar token sin UserDetails")
    void testValidateToken_WithoutUserDetails() {
        // Arrange
        String token = jwtUtil.generateToken(testEmail, testRol, testUserId);

        // Act
        Boolean isValid = jwtUtil.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Token inválido debe fallar validación")
    void testValidateToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        Boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Token con username incorrecto debe fallar validación")
    void testValidateToken_WrongUsername() {
        // Arrange
        String token = jwtUtil.generateToken(testEmail, testRol, testUserId);
        UserDetails wrongUserDetails = User.builder()
                .username("wrong@example.com")
                .password("password")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_PACIENTE")))
                .build();

        // Act
        Boolean isValid = jwtUtil.validateToken(token, wrongUserDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Extraer fecha de expiración del token")
    void testExtractExpiration() {
        // Arrange
        String token = jwtUtil.generateToken(testEmail, testRol, testUserId);

        // Act
        var expiration = jwtUtil.extractExpiration(token);

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.getTime() > System.currentTimeMillis());
    }
}


