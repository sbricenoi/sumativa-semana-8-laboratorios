package com.duoc.laboratorio.usuarios.controller;

import com.duoc.laboratorio.usuarios.dto.*;
import com.duoc.laboratorio.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para UsuarioController
 */
@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactivar seguridad para tests
@DisplayName("UsuarioController Tests")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private UsuarioDTO usuarioDTO;
    private RegistroRequest registroRequest;
    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        // UsuarioDTO de prueba
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(1L);
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setApellido("Pérez");
        usuarioDTO.setEmail("juan@test.com");
        usuarioDTO.setRol("PACIENTE");
        usuarioDTO.setActivo(1);

        // RegistroRequest de prueba
        registroRequest = new RegistroRequest();
        registroRequest.setNombre("Juan");
        registroRequest.setApellido("Pérez");
        registroRequest.setEmail("juan@test.com");
        registroRequest.setPassword("Password123*");
        registroRequest.setRol("PACIENTE");

        // LoginRequest de prueba
        loginRequest = new LoginRequest();
        loginRequest.setEmail("juan@test.com");
        loginRequest.setPassword("Password123*");

        // LoginResponse de prueba
        loginResponse = new LoginResponse();
        loginResponse.setIdUsuario(1L);
        loginResponse.setNombre("Juan");
        loginResponse.setApellido("Pérez");
        loginResponse.setEmail("juan@test.com");
        loginResponse.setRol("PACIENTE");
        loginResponse.setToken("mock-jwt-token");
        loginResponse.setMensaje("Login exitoso");
    }

    @Test
    @DisplayName("POST /api/usuarios/registro - Registrar usuario exitosamente")
    void testRegistrarUsuario_Success() throws Exception {
        // Arrange
        when(usuarioService.registrarUsuario(any(RegistroRequest.class))).thenReturn(usuarioDTO);

        // Act & Assert
        mockMvc.perform(post("/api/usuarios/registro")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.nombre").value("Juan"))
                .andExpect(jsonPath("$.data.email").value("juan@test.com"));

        verify(usuarioService, times(1)).registrarUsuario(any(RegistroRequest.class));
    }

    @Test
    @DisplayName("POST /api/usuarios/login - Login exitoso")
    void testLogin_Success() throws Exception {
        // Arrange
        when(usuarioService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/api/usuarios/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.data.email").value("juan@test.com"));

        verify(usuarioService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("GET /api/usuarios - Obtener todos los usuarios")
    @WithMockUser
    void testObtenerTodos() throws Exception {
        // Arrange
        List<UsuarioDTO> usuarios = Arrays.asList(usuarioDTO);
        when(usuarioService.obtenerTodos()).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].nombre").value("Juan"));

        verify(usuarioService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} - Obtener usuario por ID")
    @WithMockUser
    void testObtenerPorId() throws Exception {
        // Arrange
        when(usuarioService.obtenerPorId(1L)).thenReturn(usuarioDTO);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.idUsuario").value(1))
                .andExpect(jsonPath("$.data.nombre").value("Juan"));

        verify(usuarioService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /api/usuarios/rol/{rol} - Obtener usuarios por rol")
    @WithMockUser
    void testObtenerPorRol() throws Exception {
        // Arrange
        List<UsuarioDTO> usuarios = Arrays.asList(usuarioDTO);
        when(usuarioService.obtenerPorRol("PACIENTE")).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/rol/PACIENTE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].rol").value("PACIENTE"));

        verify(usuarioService, times(1)).obtenerPorRol("PACIENTE");
    }

    @Test
    @DisplayName("GET /api/usuarios/buscar - Buscar usuarios")
    @WithMockUser
    void testBuscar() throws Exception {
        // Arrange
        List<UsuarioDTO> usuarios = Arrays.asList(usuarioDTO);
        when(usuarioService.buscarPorNombreOApellido("Juan")).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/buscar")
                .param("texto", "Juan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray());

        verify(usuarioService, times(1)).buscarPorNombreOApellido("Juan");
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} - Actualizar usuario")
    @WithMockUser
    void testActualizarUsuario() throws Exception {
        // Arrange
        when(usuarioService.actualizarUsuario(eq(1L), any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        // Act & Assert
        mockMvc.perform(put("/api/usuarios/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.nombre").value("Juan"));

        verify(usuarioService, times(1)).actualizarUsuario(eq(1L), any(UsuarioDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} - Eliminar usuario (soft delete)")
    @WithMockUser
    void testEliminarUsuario() throws Exception {
        // Arrange
        doNothing().when(usuarioService).eliminarUsuario(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/usuarios/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        verify(usuarioService, times(1)).eliminarUsuario(1L);
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id}/permanente - Eliminar usuario permanentemente")
    @WithMockUser(roles = "ADMINISTRADOR")
    void testEliminarUsuarioPermanente() throws Exception {
        // Arrange
        doNothing().when(usuarioService).eliminarUsuarioPermanente(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/usuarios/1/permanente")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        verify(usuarioService, times(1)).eliminarUsuarioPermanente(1L);
    }

    @Test
    @DisplayName("GET /api/usuarios/health - Health check")
    void testHealthCheck() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/usuarios/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").value("OK"));
    }
}


