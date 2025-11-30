package com.ms_auth.auth.controller;

import com.ms_auth.auth.dto.AuthUserDto;
import com.ms_auth.auth.dto.UserProfileDto;
import com.ms_auth.auth.dto.LoginDto;
import com.ms_auth.auth.dto.TokenDto;
import com.ms_auth.auth.model.Usuario;
import com.ms_auth.auth.service.AuthService;
import com.ms_auth.auth.dto.UpdateProfileDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*") // Permite todas las apps móviles
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Usuario> create(@RequestBody AuthUserDto dto) {
        return ResponseEntity.ok(authService.save(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDto> validate(@RequestParam String token) {
        return ResponseEntity.ok(authService.validate(token));
    }

    @GetMapping("/perfil")
    public ResponseEntity<UserProfileDto> verPerfil(@RequestParam String email) {
        UserProfileDto perfil = authService.obtenerPerfil(email);
        return ResponseEntity.ok(perfil);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserProfileDto>> listarUsuarios() {
        return ResponseEntity.ok(authService.listarTodos());
    }

    @PutMapping("/update")
    public ResponseEntity<UserProfileDto> actualizar(@RequestParam String email, @RequestBody AuthUserDto dto) {
        return ResponseEntity.ok(authService.actualizarUsuario(email, dto));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> actualizarMiPerfil(@RequestBody UpdateProfileDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailDelUsuario = auth.getName();
        UserProfileDto perfilActualizado = authService.actualizarPerfilPropio(emailDelUsuario, dto);
        return ResponseEntity.ok(perfilActualizado);
    }
}