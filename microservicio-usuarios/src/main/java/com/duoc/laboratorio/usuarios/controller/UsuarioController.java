package com.duoc.laboratorio.usuarios.controller;

import com.duoc.laboratorio.usuarios.dto.*;
import com.duoc.laboratorio.usuarios.exception.BadRequestException;
import com.duoc.laboratorio.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "API para gestión de usuarios y autenticación")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Registra un nuevo usuario en el sistema con email único y contraseña encriptada"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Usuario registrado exitosamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o email duplicado"
        )
    })
    public ResponseEntity<ApiResponse<UsuarioDTO>> registrarUsuario(@Valid @RequestBody RegistroRequest request) {
        UsuarioDTO usuarioCreado = usuarioService.registrarUsuario(request);
        ApiResponse<UsuarioDTO> response = ApiResponse.success("Usuario registrado exitosamente", usuarioCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario y devuelve un token JWT válido por 24 horas"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Login exitoso, devuelve token JWT",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Credenciales incorrectas"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Usuario inactivo"
        )
    })
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = usuarioService.login(request);
        ApiResponse<LoginResponse> response = ApiResponse.success("Inicio de sesión exitoso", loginResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Retorna la lista completa de usuarios registrados en el sistema",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios obtenida exitosamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado - Token inválido o ausente"
        )
    })
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerTodos() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
        ApiResponse<List<UsuarioDTO>> response = ApiResponse.success("Lista de usuarios obtenida exitosamente", usuarios);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Retorna los datos de un usuario específico por su ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado"
        )
    })
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerPorId(
        @Parameter(description = "ID del usuario", required = true)
        @PathVariable Long id
    ) {
        UsuarioDTO usuario = usuarioService.obtenerPorId(id);
        ApiResponse<UsuarioDTO> response = ApiResponse.success("Usuario encontrado", usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerPorRol(@PathVariable String rol) {
        List<UsuarioDTO> usuarios = usuarioService.obtenerPorRol(rol);
        ApiResponse<List<UsuarioDTO>> response = ApiResponse.success("Usuarios con rol " + rol + " obtenidos exitosamente", usuarios);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> buscar(@RequestParam String texto) {
        List<UsuarioDTO> usuarios = usuarioService.buscarPorNombreOApellido(texto);
        ApiResponse<List<UsuarioDTO>> response = ApiResponse.success("Búsqueda completada", usuarios);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar usuario",
        description = "Actualiza los datos de un usuario existente",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<UsuarioDTO>> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        ApiResponse<UsuarioDTO> response = ApiResponse.success("Usuario actualizado exitosamente", usuarioActualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Desactivar usuario",
        description = "Marca un usuario como inactivo (eliminación lógica)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Object>> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        ApiResponse<Object> response = ApiResponse.success("Usuario eliminado exitosamente", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/permanente")
    @Operation(
        summary = "Eliminar usuario permanentemente",
        description = "Elimina un usuario físicamente de la base de datos (requiere rol ADMINISTRADOR)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Object>> eliminarUsuarioPermanente(@PathVariable Long id) {
        usuarioService.eliminarUsuarioPermanente(id);
        ApiResponse<Object> response = ApiResponse.success("Usuario eliminado permanentemente", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperar-password")
    @Operation(
        summary = "Solicitar recuperación de contraseña",
        description = "Envía un email con instrucciones para recuperar la contraseña"
    )
    public ResponseEntity<ApiResponse<Object>> recuperarPassword(@Valid @RequestBody RecuperarPasswordRequest request) {
        usuarioService.recuperarPassword(request.getEmail());
        ApiResponse<Object> response = ApiResponse.success("Se han enviado instrucciones al email proporcionado", null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cambiar-password")
    @Operation(
        summary = "Cambiar contraseña",
        description = "Permite a un usuario cambiar su contraseña actual",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Object>> cambiarPassword(
        @PathVariable Long id,
        @Valid @RequestBody CambiarPasswordRequest request
    ) {
        if (!request.getPasswordNueva().equals(request.getPasswordConfirmacion())) {
            throw new BadRequestException("Las contraseñas no coinciden");
        }
        usuarioService.cambiarPassword(id, request.getPasswordActual(), request.getPasswordNueva());
        ApiResponse<Object> response = ApiResponse.success("Contraseña actualizada exitosamente", null);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activar")
    @Operation(
        summary = "Activar usuario",
        description = "Activa un usuario inactivo (requiere rol ADMINISTRADOR)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Object>> activarUsuario(@PathVariable Long id) {
        usuarioService.activarDesactivarUsuario(id, true);
        ApiResponse<Object> response = ApiResponse.success("Usuario activado exitosamente", null);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desactivar")
    @Operation(
        summary = "Desactivar usuario",
        description = "Desactiva un usuario (requiere rol ADMINISTRADOR)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Object>> desactivarUsuario(@PathVariable Long id) {
        usuarioService.activarDesactivarUsuario(id, false);
        ApiResponse<Object> response = ApiResponse.success("Usuario desactivado exitosamente", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(
        summary = "Health check",
        description = "Verifica el estado del microservicio de usuarios"
    )
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        ApiResponse<String> response = ApiResponse.success("Microservicio de Usuarios funcionando correctamente", "OK");
        return ResponseEntity.ok(response);
    }
}
