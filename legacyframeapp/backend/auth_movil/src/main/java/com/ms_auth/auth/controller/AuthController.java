package com.ms_auth.auth.controller;

import com.ms_auth.auth.dto.AuthUserDto;
import com.ms_auth.auth.dto.UserProfileDto;
import com.ms_auth.auth.dto.LoginDto;
import com.ms_auth.auth.dto.TokenDto;
import com.ms_auth.auth.model.Usuario;
import com.ms_auth.auth.service.AuthService;
import com.ms_auth.auth.dto.UpdateProfileDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*") // Permite todas las apps móviles
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Registro, login, validación de token y administración de perfil")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
        @Operation(
            summary = "Registrar usuario",
            description = "Crea un usuario nuevo para el sistema."
        )
    public ResponseEntity<Usuario> create(@RequestBody AuthUserDto dto) {
        return ResponseEntity.ok(authService.save(dto));
    }

    @PostMapping("/login")
        @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y retorna un token (JWT u otro) según la implementación."
        )
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/validate")
        @Operation(
            summary = "Validar token",
            description = "Valida un token recibido y retorna el resultado/estado de validación."
        )
        public ResponseEntity<TokenDto> validate(
            @Parameter(description = "Token a validar")
            @RequestParam String token
        ) {
        return ResponseEntity.ok(authService.validate(token));
    }

    @GetMapping("/perfil")
        @Operation(
            summary = "Obtener perfil por email",
            description = "Obtiene los datos de perfil del usuario a partir de su email."
        )
        public ResponseEntity<UserProfileDto> verPerfil(
            @Parameter(description = "Email del usuario")
            @RequestParam String email
        ) {
        UserProfileDto perfil = authService.obtenerPerfil(email);
        return ResponseEntity.ok(perfil);
    }

    @GetMapping("/list")
        @Operation(
            summary = "Listar usuarios",
            description = "Devuelve el listado de usuarios del sistema."
        )
    public ResponseEntity<List<UserProfileDto>> listarUsuarios() {
        return ResponseEntity.ok(authService.listarTodos());
    }

    @PutMapping("/update")
        @Operation(
            summary = "Actualizar usuario por email",
            description = "Actualiza los datos del usuario indicado por email."
        )
        public ResponseEntity<UserProfileDto> actualizar(
            @Parameter(description = "Email del usuario a actualizar")
            @RequestParam String email,
            @RequestBody AuthUserDto dto
        ) {
        return ResponseEntity.ok(authService.actualizarUsuario(email, dto));
    }

    @PutMapping("/profile")
        @Operation(
            summary = "Actualizar mi perfil",
            description = "Actualiza el perfil del usuario autenticado (toma el email desde el contexto de seguridad)."
        )
    public ResponseEntity<UserProfileDto> actualizarMiPerfil(@RequestBody UpdateProfileDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailDelUsuario = auth.getName();
        UserProfileDto perfilActualizado = authService.actualizarPerfilPropio(emailDelUsuario, dto);
        return ResponseEntity.ok(perfilActualizado);
    }
}