package com.ms_auth.auth.service;

import com.ms_auth.auth.dto.AuthUserDto;
import com.ms_auth.auth.dto.LoginDto;
import com.ms_auth.auth.dto.TokenDto;
import com.ms_auth.auth.dto.UpdateProfileDto;
import com.ms_auth.auth.dto.UserProfileDto;
import com.ms_auth.auth.exception.DuplicateFieldException;
import com.ms_auth.auth.exception.InvalidCredentialsException;
import com.ms_auth.auth.exception.UserNotFoundException;
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
        if (dto == null) throw new IllegalArgumentException("Datos de registro inválidos");

        String email = dto.getEmail() != null ? dto.getEmail().trim().toLowerCase() : null;
        String normalizedRut = dto.getRut() != null ? dto.getRut().replaceAll("[^0-9]", "") : null;
        String telefono = dto.getTelefono() != null ? dto.getTelefono().trim() : null;
        String dv = dto.getDv() != null ? dto.getDv().trim() : null;

        if (email == null || email.isBlank()) throw new IllegalArgumentException("El correo es obligatorio");
        if (normalizedRut == null || normalizedRut.isBlank()) throw new IllegalArgumentException("El RUT es obligatorio");
        if (dv == null || dv.isBlank()) throw new IllegalArgumentException("El dígito verificador es obligatorio");
        if (telefono == null || telefono.isBlank()) throw new IllegalArgumentException("El teléfono es obligatorio");

        // Validar duplicados (mensajes claros)
        if (usuarioRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new DuplicateFieldException("email", "El correo ya está registrado");
        }
        if (usuarioRepository.findByRut(normalizedRut).isPresent()) {
            throw new DuplicateFieldException("rut", "El RUT ya está registrado");
        }
        if (usuarioRepository.findByTelefono(telefono).isPresent()) {
            throw new DuplicateFieldException("telefono", "El número de teléfono ya está registrado");
        }

        // Validar que las contraseñas coincidan
        if (dto.getPassword() == null || dto.getConfirmPassword() == null || !dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setRut(normalizedRut);
        usuario.setDv(dv);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol("USER");

        return usuarioRepository.save(usuario);
    }

    public TokenDto login(LoginDto dto) {
        // Si se envía rut, autenticamos por RUT (permite rut con o sin DV, con guión o puntos)
        if (dto.getRut() != null && !dto.getRut().isBlank()) {
            String raw = dto.getRut().trim();
            String dvFromRut = null;
            if (raw.contains("-")) {
                String[] parts = raw.split("-", 2);
                raw = parts[0];
                dvFromRut = parts[1];
            }
            // Normalizar: eliminar puntos y espacios
            String normalizedRut = raw.replaceAll("[^0-9]", "");

            Optional<Usuario> userOpt = usuarioRepository.findByRut(normalizedRut);
            if (userOpt.isEmpty()) {
                throw new UserNotFoundException("El RUT no está registrado");
            }
            Usuario usuario = userOpt.get();

            // Si se proporcionó DV (en dto.dv o en rut), verificar coincidencia
            String dvProvided = dto.getDv();
            if ((dvProvided == null || dvProvided.isBlank()) && dvFromRut != null) dvProvided = dvFromRut;
            if (dvProvided != null && !dvProvided.isBlank()) {
                if (!usuario.getDv().equalsIgnoreCase(dvProvided.trim())) {
                    throw new InvalidCredentialsException("Dígito verificador incorrecto");
                }
            }

            if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
                throw new InvalidCredentialsException("Contraseña incorrecta");
            }

            return new TokenDto(jwtProvider.createToken(usuario));
        }

        // Por defecto autenticación por email
        Optional<Usuario> userOpt = usuarioRepository.findByEmail(dto.getEmail());
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("El correo no está registrado");
        }
        Usuario usuario = userOpt.get();
        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new InvalidCredentialsException("Contraseña incorrecta");
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
        perfil.setDireccion(usuario.getDireccion());

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
        if (datosNuevos.getTelefono() != null && !datosNuevos.getTelefono().isBlank()) {
            String telefono = datosNuevos.getTelefono().trim();
            if (!telefono.equals(usuario.getTelefono())) {
                Optional<Usuario> existing = usuarioRepository.findByTelefono(telefono);
                if (existing.isPresent() && !existing.get().getId().equals(usuario.getId())) {
                    throw new DuplicateFieldException("telefono", "El número de teléfono ya está registrado");
                }
            }
            usuario.setTelefono(telefono);
        }

        if (datosNuevos.getPassword() != null && !datosNuevos.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(datosNuevos.getPassword()));
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        UserProfileDto perfil = new UserProfileDto();
        perfil.setNombre(usuarioGuardado.getNombre());
        perfil.setApellido(usuarioGuardado.getApellido());
        perfil.setEmail(usuarioGuardado.getEmail());
        perfil.setTelefono(usuarioGuardado.getTelefono());
        perfil.setDireccion(usuarioGuardado.getDireccion());

        return perfil;
    }

    public UserProfileDto actualizarPerfilPropio(String emailDelToken, UpdateProfileDto dto) {
        Usuario usuario = usuarioRepository.findByEmail(emailDelToken)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        if (dto.getTelefono() != null && !dto.getTelefono().isBlank()) {
            String telefono = dto.getTelefono().trim();
            if (!telefono.equals(usuario.getTelefono())) {
                Optional<Usuario> existing = usuarioRepository.findByTelefono(telefono);
                if (existing.isPresent() && !existing.get().getId().equals(usuario.getId())) {
                    throw new DuplicateFieldException("telefono", "El número de teléfono ya está registrado");
                }
            }
            usuario.setTelefono(telefono);
        }
        usuario.setDireccion(dto.getDireccion());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) throw new IllegalArgumentException("Las contraseñas nuevas no coinciden");
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        UserProfileDto perfil = new UserProfileDto();
        perfil.setNombre(usuarioGuardado.getNombre());
        perfil.setApellido(usuarioGuardado.getApellido());
        perfil.setEmail(usuarioGuardado.getEmail());
        perfil.setTelefono(usuarioGuardado.getTelefono());
        perfil.setDireccion(usuarioGuardado.getDireccion());

        return perfil;
    }
}