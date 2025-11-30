package com.ms_auth.auth.service;

import com.ms_auth.auth.dto.AuthUserDto;
import com.ms_auth.auth.dto.LoginDto;
import com.ms_auth.auth.dto.TokenDto;
import com.ms_auth.auth.dto.UpdateProfileDto;
import com.ms_auth.auth.dto.UserProfileDto;
import com.ms_auth.auth.model.Usuario;
import com.ms_auth.auth.repository.UsuarioRepository;
import com.ms_auth.auth.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public Usuario save(AuthUserDto dto) {
        // Validar si el email ya existe
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        // Validar si el RUT ya existe
        if (usuarioRepository.findByRut(dto.getRut()).isPresent()) {
            throw new RuntimeException("El RUT ya está registrado");
        }

        // Validar que las contraseñas coincidan
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setRut(dto.getRut());
        usuario.setDv(dto.getDv());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol("USER");

        return usuarioRepository.save(usuario);
    }

    public TokenDto login(LoginDto dto) {
        Optional<Usuario> userOpt = usuarioRepository.findByEmail(dto.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        Usuario usuario = userOpt.get();
        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        return new TokenDto(jwtProvider.createToken(usuario));
    }

    public TokenDto validate(String token) {
        if (!jwtProvider.validateToken(token)) {
            throw new RuntimeException("Token inválido");
        }
        return new TokenDto(token);
    }

    public UserProfileDto obtenerPerfil(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserProfileDto perfil = new UserProfileDto();
        perfil.setNombre(usuario.getNombre());
        perfil.setApellido(usuario.getApellido());
        perfil.setEmail(usuario.getEmail());
        perfil.setTelefono(usuario.getTelefono());

        return perfil;
    }

    public List<UserProfileDto> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios.stream().map(usuario -> {
            UserProfileDto dto = new UserProfileDto();
            dto.setNombre(usuario.getNombre());
            dto.setApellido(usuario.getApellido());
            dto.setEmail(usuario.getEmail());
            dto.setTelefono(usuario.getTelefono());
            return dto;
        }).collect(Collectors.toList());
    }

    public UserProfileDto actualizarUsuario(String email, AuthUserDto datosNuevos) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(datosNuevos.getNombre());
        usuario.setApellido(datosNuevos.getApellido());
        usuario.setTelefono(datosNuevos.getTelefono());

        if (datosNuevos.getPassword() != null && !datosNuevos.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(datosNuevos.getPassword()));
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        UserProfileDto perfil = new UserProfileDto();
        perfil.setNombre(usuarioGuardado.getNombre());
        perfil.setApellido(usuarioGuardado.getApellido());
        perfil.setEmail(usuarioGuardado.getEmail());
        perfil.setTelefono(usuarioGuardado.getTelefono());

        return perfil;
    }

    public UserProfileDto actualizarPerfilPropio(String emailDelToken, UpdateProfileDto dto) {
        Usuario usuario = usuarioRepository.findByEmail(emailDelToken)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setTelefono(dto.getTelefono());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new RuntimeException("Las contraseñas nuevas no coinciden");
            }
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        UserProfileDto perfil = new UserProfileDto();
        perfil.setNombre(usuarioGuardado.getNombre());
        perfil.setApellido(usuarioGuardado.getApellido());
        perfil.setEmail(usuarioGuardado.getEmail());
        perfil.setTelefono(usuarioGuardado.getTelefono());

        return perfil;
    }
}