package com.ms_contacto.contacto;

import com.ms_contacto.contacto.model.Contacto;
import com.ms_contacto.contacto.repository.ContactoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContactoApplicationTests {

	@Autowired
	private ContactoRepository contactoRepository;

	@Test
	void contextLoads() {
		// Verifica que el contexto de Spring Boot se carga correctamente
		assertNotNull(contactoRepository);
	}

	@Test
	void guardarMensajeContacto() {
		// Guarda un mensaje de contacto y verifica que se persiste correctamente
		Contacto contacto = new Contacto();
		contacto.setNombre("Juan Pérez");
		contacto.setEmail("juan@test.com");
		contacto.setMensaje("Consulta sobre productos");
		contacto.setFechaEnvio(LocalDateTime.now());

		Contacto savedContacto = contactoRepository.save(contacto);

		assertNotNull(savedContacto.getId());
		assertEquals("Juan Pérez", savedContacto.getNombre());
		assertEquals("juan@test.com", savedContacto.getEmail());
	}

	@Test
	void listarTodosLosMensajes() {
		// Guarda varios mensajes y verifica que se pueden listar
		Contacto contacto1 = new Contacto();
		contacto1.setNombre("María");
		contacto1.setEmail("maria@test.com");
		contacto1.setMensaje("Mensaje 1");
		contacto1.setFechaEnvio(LocalDateTime.now());
		contactoRepository.save(contacto1);

		Contacto contacto2 = new Contacto();
		contacto2.setNombre("Pedro");
		contacto2.setEmail("pedro@test.com");
		contacto2.setMensaje("Mensaje 2");
		contacto2.setFechaEnvio(LocalDateTime.now());
		contactoRepository.save(contacto2);

		List<Contacto> mensajes = contactoRepository.findAll();

		assertTrue(mensajes.size() >= 2);
	}

}
