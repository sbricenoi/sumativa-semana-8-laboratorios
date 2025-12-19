package com.duoc.laboratorio.usuarios.service;

import com.duoc.laboratorio.usuarios.dto.*;
import com.duoc.laboratorio.usuarios.exception.BadRequestException;
import com.duoc.laboratorio.usuarios.exception.ResourceNotFoundException;
import com.duoc.laboratorio.usuarios.model.Usuario;
import com.duoc.laboratorio.usuarios.repository.UsuarioRepository;
import com.duoc.laboratorio.usuarios.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public UsuarioDTO registrarUsuario(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado en el sistema");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(request.getRol());
        usuario.setActivo(1);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioGuardado);
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (usuario.getActivo() == 0) {
            throw new BadRequestException("Usuario inactivo. Contacte al administrador");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new BadRequestException("Credenciales incorrectas");
        }

        // Generar token JWT
        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol(), usuario.getIdUsuario());

        return new LoginResponse(
            usuario.getIdUsuario(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getEmail(),
            usuario.getRol(),
            token,
            "Inicio de sesión exitoso"
        );
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return convertirADTO(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerPorRol(String rol) {
        return usuarioRepository.findByRol(rol).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        if (!usuario.getEmail().equals(usuarioDTO.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new BadRequestException("El email ya está registrado en el sistema");
        }

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setRol(usuarioDTO.getRol());
        usuario.setActivo(usuarioDTO.getActivo());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioActualizado);
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        usuario.setActivo(0);
        usuarioRepository.save(usuario);
    }

    public void eliminarUsuarioPermanente(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorNombreOApellido(String texto) {
        return usuarioRepository.buscarPorNombreOApellido(texto).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    public void recuperarPassword(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        
        if (usuario.getActivo() == 0) {
            throw new BadRequestException("Usuario inactivo. Contacte al administrador");
        }
        
        // TODO: Implementar envío de email con token de recuperación
        // Por ahora, solo validamos que el usuario existe
        // En producción: generar token temporal, enviar email, etc.
    }

    public void cambiarPassword(Long idUsuario, String passwordActual, String passwordNueva) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        
        // Verificar contraseña actual
        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new BadRequestException("La contraseña actual es incorrecta");
        }
        
        // Actualizar contraseña
        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        usuarioRepository.save(usuario);
    }

    public void activarDesactivarUsuario(Long idUsuario, boolean activar) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        
        usuario.setActivo(activar ? 1 : 0);
        usuarioRepository.save(usuario);
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        dto.setActivo(usuario.getActivo());
        return dto;
    }
}
