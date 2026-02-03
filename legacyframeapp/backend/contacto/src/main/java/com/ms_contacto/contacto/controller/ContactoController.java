package com.ms_contacto.contacto.controller;

import com.ms_contacto.contacto.model.Contacto;
import com.ms_contacto.contacto.service.ContactoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/contactos")
@Tag(name = "Contactos", description = "ABM de contactos")
public class ContactoController {

    private final ContactoService contactoService;

    public ContactoController(ContactoService contactoService) {
        this.contactoService = contactoService;
    }

    @PostMapping
        @Operation(
            summary = "Crear contacto",
            description = "Crea un nuevo contacto y retorna el recurso creado."
        )
    public ResponseEntity<Contacto> createContacto(@Valid @RequestBody Contacto contacto) {
        Contacto saved = contactoService.saveContacto(contacto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping
        @Operation(
            summary = "Listar contactos",
            description = "Devuelve todos los contactos registrados."
        )
    public ResponseEntity<List<Contacto>> getAllContactos() {
        return ResponseEntity.ok(contactoService.getAllContactos());
    }

    @GetMapping("/{id}")
        @Operation(
            summary = "Obtener contacto por id",
            description = "Devuelve un contacto específico por su identificador."
        )
        public ResponseEntity<Contacto> getContactoById(
            @Parameter(description = "ID del contacto")
            @PathVariable Long id
        ) {
        Contacto c = contactoService.getContactoById(id);
        return ResponseEntity.ok(c);
    }

    @PutMapping("/{id}")
        @Operation(
            summary = "Actualizar contacto",
            description = "Actualiza los datos del contacto indicado por id."
        )
        public ResponseEntity<Contacto> updateContacto(
            @Parameter(description = "ID del contacto")
            @PathVariable Long id,
            @Valid @RequestBody Contacto contactoDetails
        ) {
        Contacto updated = contactoService.updateContacto(id, contactoDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
        @Operation(
            summary = "Eliminar contacto",
            description = "Elimina el contacto indicado por id."
        )
        public ResponseEntity<Void> deleteContacto(
            @Parameter(description = "ID del contacto")
            @PathVariable Long id
        ) {
        contactoService.deleteContacto(id);
        return ResponseEntity.noContent().build();
    }
}