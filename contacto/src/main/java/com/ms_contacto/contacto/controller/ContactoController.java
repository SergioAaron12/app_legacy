package com.ms_contacto.contacto.controller;

import com.ms_contacto.contacto.model.Contacto;
import com.ms_contacto.contacto.service.ContactoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/contactos")
public class ContactoController {

    private final ContactoService contactoService;

    public ContactoController(ContactoService contactoService) {
        this.contactoService = contactoService;
    }

    @PostMapping
    public ResponseEntity<Contacto> createContacto(@Valid @RequestBody Contacto contacto) {
        Contacto saved = contactoService.saveContacto(contacto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Contacto>> getAllContactos() {
        return ResponseEntity.ok(contactoService.getAllContactos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contacto> getContactoById(@PathVariable Long id) {
        Contacto c = contactoService.getContactoById(id);
        return ResponseEntity.ok(c);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contacto> updateContacto(@PathVariable Long id, @Valid @RequestBody Contacto contactoDetails) {
        Contacto updated = contactoService.updateContacto(id, contactoDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContacto(@PathVariable Long id) {
        contactoService.deleteContacto(id);
        return ResponseEntity.noContent().build();
    }
}