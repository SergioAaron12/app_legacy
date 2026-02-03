package com.ms_contacto.contacto.service;

import com.ms_contacto.contacto.exception.ResourceNotFoundException;
import com.ms_contacto.contacto.model.Contacto;
import com.ms_contacto.contacto.repository.ContactoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContactoService {

    private static final Logger log = LoggerFactory.getLogger(ContactoService.class);

    private final ContactoRepository contactoRepository;

    public ContactoService(ContactoRepository contactoRepository) {
        this.contactoRepository = contactoRepository;
    }

    public Contacto saveContacto(Contacto contacto) {
        log.debug("Guardando contacto: {}", contacto);
        return contactoRepository.save(contacto);
    }

    public List<Contacto> getAllContactos() {
        return contactoRepository.findAll();
    }

    public Contacto getContactoById(Long id) {
        return contactoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contacto no encontrado con ID: " + id));
    }

    public Contacto updateContacto(Long id, Contacto contactoDetails) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contacto no encontrado con ID: " + id));

        contacto.setNombre(contactoDetails.getNombre());
        contacto.setEmail(contactoDetails.getEmail());
        contacto.setMensaje(contactoDetails.getMensaje());

        Contacto updated = contactoRepository.save(contacto);
        log.debug("Contacto actualizado: {}", updated);
        return updated;
    }

    public void deleteContacto(Long id) {
        if (!contactoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contacto no encontrado con ID: " + id);
        }
        contactoRepository.deleteById(id);
        log.debug("Contacto eliminado con ID: {}", id);
    }
}