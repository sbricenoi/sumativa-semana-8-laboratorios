package com.duoc.laboratorio.usuarios.service;

import com.duoc.laboratorio.usuarios.dto.*;
import com.duoc.laboratorio.usuarios.exception.BadRequestException;
import com.duoc.laboratorio.usuarios.exception.ResourceNotFoundException;
import com.duoc.laboratorio.usuarios.model.Usuario;
import com.duoc.laboratorio.usuarios.repository.UsuarioRepository;
import com.duoc.laboratorio.usuarios.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para UsuarioService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService Tests")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioTest;
    private RegistroRequest registroRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        // Usuario de prueba
        usuarioTest = new Usuario();
        usuarioTest.setIdUsuario(1L);
        usuarioTest.setNombre("Juan");
        usuarioTest.setApellido("Pérez");
        usuarioTest.setEmail("juan@test.com");
        usuarioTest.setPassword("$2a$10$encodedPassword");
        usuarioTest.setRol("PACIENTE");
        usuarioTest.setActivo(1);

        // Request de registro
        registroRequest = new RegistroRequest();
        registroRequest.setNombre("Juan");
        registroRequest.setApellido("Pérez");
        registroRequest.setEmail("juan@test.com");
        registroRequest.setPassword("Password123*");
        registroRequest.setRol("PACIENTE");

        // Request de login
        loginRequest = new LoginRequest();
        loginRequest.setEmail("juan@test.com");
        loginRequest.setPassword("Password123*");
    }

    @Test
    @DisplayName("Registrar usuario exitosamente")
    void testRegistrarUsuario_Success() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);

        // Act
        UsuarioDTO result = usuarioService.registrarUsuario(registroRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("juan@test.com", result.getEmail());
        verify(usuarioRepository, times(1)).existsByEmail(anyString());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Registrar usuario con email duplicado debe lanzar BadRequestException")
    void testRegistrarUsuario_EmailDuplicado() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> usuarioService.registrarUsuario(registroRequest)
        );
        
        assertEquals("El email ya está registrado en el sistema", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Login exitoso debe retornar token JWT")
    void testLogin_Success() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioTest));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), anyLong())).thenReturn("mock-jwt-token");

        // Act
        LoginResponse result = usuarioService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals("juan@test.com", result.getEmail());
        assertEquals("mock-jwt-token", result.getToken());
        assertEquals("Inicio de sesión exitoso", result.getMensaje());
        verify(jwtUtil, times(1)).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("Login con usuario no existente debe lanzar ResourceNotFoundException")
    void testLogin_UsuarioNoExiste() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> usuarioService.login(loginRequest)
        );
        
        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Login con contraseña incorrecta debe lanzar BadRequestException")
    void testLogin_PasswordIncorrecta() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioTest));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> usuarioService.login(loginRequest)
        );
        
        assertEquals("Credenciales incorrectas", exception.getMessage());
    }

    @Test
    @DisplayName("Login con usuario inactivo debe lanzar BadRequestException")
    void testLogin_UsuarioInactivo() {
        // Arrange
        usuarioTest.setActivo(0);
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioTest));

        // Act & Assert
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> usuarioService.login(loginRequest)
        );
        
        assertTrue(exception.getMessage().contains("inactivo"));
    }

    @Test
    @DisplayName("Obtener todos los usuarios")
    void testObtenerTodos() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuarioTest);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<UsuarioDTO> result = usuarioService.obtenerTodos();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener usuario por ID existente")
    void testObtenerPorId_Existente() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));

        // Act
        UsuarioDTO result = usuarioService.obtenerPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("juan@test.com", result.getEmail());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Obtener usuario por ID no existente debe lanzar ResourceNotFoundException")
    void testObtenerPorId_NoExistente() {
        // Arrange
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> usuarioService.obtenerPorId(999L)
        );
        
        assertTrue(exception.getMessage().contains("no encontrado"));
    }

    @Test
    @DisplayName("Obtener usuarios por rol")
    void testObtenerPorRol() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuarioTest);
        when(usuarioRepository.findByRol("PACIENTE")).thenReturn(usuarios);

        // Act
        List<UsuarioDTO> result = usuarioService.obtenerPorRol("PACIENTE");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PACIENTE", result.get(0).getRol());
        verify(usuarioRepository, times(1)).findByRol("PACIENTE");
    }

    @Test
    @DisplayName("Actualizar usuario exitosamente")
    void testActualizarUsuario_Success() {
        // Arrange
        UsuarioDTO updateDTO = new UsuarioDTO();
        updateDTO.setNombre("Juan Carlos");
        updateDTO.setApellido("Pérez");
        updateDTO.setEmail("juan@test.com");
        updateDTO.setRol("MEDICO");
        updateDTO.setActivo(1);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);

        // Act
        UsuarioDTO result = usuarioService.actualizarUsuario(1L, updateDTO);

        // Assert
        assertNotNull(result);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Actualizar usuario con email duplicado debe lanzar BadRequestException")
    void testActualizarUsuario_EmailDuplicado() {
        // Arrange
        UsuarioDTO updateDTO = new UsuarioDTO();
        updateDTO.setEmail("otro@test.com");
        updateDTO.setNombre("Juan");
        updateDTO.setApellido("Pérez");
        updateDTO.setRol("PACIENTE");
        updateDTO.setActivo(1);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        when(usuarioRepository.existsByEmail("otro@test.com")).thenReturn(true);

        // Act & Assert
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> usuarioService.actualizarUsuario(1L, updateDTO)
        );
        
        assertTrue(exception.getMessage().contains("ya está registrado"));
    }

    @Test
    @DisplayName("Eliminar usuario (soft delete)")
    void testEliminarUsuario() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);

        // Act
        usuarioService.eliminarUsuario(1L);

        // Assert
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Eliminar usuario permanentemente")
    void testEliminarUsuarioPermanente() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        // Act
        usuarioService.eliminarUsuarioPermanente(1L);

        // Assert
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Buscar usuarios por nombre o apellido")
    void testBuscarPorNombreOApellido() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuarioTest);
        when(usuarioRepository.buscarPorNombreOApellido("Juan")).thenReturn(usuarios);

        // Act
        List<UsuarioDTO> result = usuarioService.buscarPorNombreOApellido("Juan");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(usuarioRepository, times(1)).buscarPorNombreOApellido("Juan");
    }
}


