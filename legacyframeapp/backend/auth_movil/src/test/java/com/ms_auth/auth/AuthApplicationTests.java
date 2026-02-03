package com.ms_auth.auth;

import com.ms_auth.auth.model.Usuario;
import com.ms_auth.auth.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthApplicationTests {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
		// Verifica que el contexto de Spring Boot se carga correctamente
		assertNotNull(usuarioRepository);
		assertNotNull(passwordEncoder);
	}

	@Test
	void registrarNuevoUsuario() {
		// Registra un nuevo usuario y verifica que se guarda correctamente
		Usuario usuario = new Usuario();
		usuario.setNombre("Test");
		usuario.setApellido("Usuario");
		usuario.setEmail("test" + System.currentTimeMillis() + "@duoc.cl");
		usuario.setRut("12345678");
		usuario.setDv("9");
		usuario.setTelefono("987654321");
		usuario.setPassword(passwordEncoder.encode("Pass123!"));
		usuario.setRol("CLIENTE");

		Usuario savedUsuario = usuarioRepository.save(usuario);

		assertNotNull(savedUsuario.getId());
		assertEquals("Test", savedUsuario.getNombre());
		assertTrue(passwordEncoder.matches("Pass123!", savedUsuario.getPassword()));
	}

	@Test
	void buscarUsuarioPorEmail() {
		// Crea un usuario y verifica que se puede buscar por email
		String uniqueEmail = "search" + System.currentTimeMillis() + "@test.com";
		
		Usuario usuario = new Usuario();
		usuario.setNombre("Búsqueda");
		usuario.setApellido("Test");
		usuario.setEmail(uniqueEmail);
		usuario.setRut("11111111");
		usuario.setDv("1");
		usuario.setTelefono("912345678");
		usuario.setPassword(passwordEncoder.encode("Test123!"));
		usuario.setRol("CLIENTE");
		usuarioRepository.save(usuario);

		Optional<Usuario> found = usuarioRepository.findByEmail(uniqueEmail);

		assertTrue(found.isPresent());
		assertEquals("Búsqueda", found.get().getNombre());
	}

}
