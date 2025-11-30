package com.ms_auth.auth;

import com.ms_auth.auth.model.Usuario;
import com.ms_auth.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Optional<Usuario> admin = usuarioRepository.findByEmail("admin@legacyframes.cl");

            if (admin.isEmpty()) {
                Usuario newAdmin = new Usuario();
                newAdmin.setNombre("Admin");
                newAdmin.setApellido("Sistema");
                newAdmin.setEmail("admin@legacyframes.cl");
                newAdmin.setPassword(passwordEncoder.encode("admin123!"));
                newAdmin.setRut("12345678"); // Agregado
                newAdmin.setDv("9"); // Agregado
                newAdmin.setTelefono("987654321"); // Cambio: debe ser 9 dígitos
                newAdmin.setRol("ADMIN");
                
                usuarioRepository.save(newAdmin);
                System.out.println(">>> USUARIO ADMIN CREADO AUTOMÁTICAMENTE <<<");
            }
        };
    }
}