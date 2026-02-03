package com.ms_contacto.contacto.repository;

import com.ms_contacto.contacto.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// El Long es el tipo de la clave primaria de la entidad Contacto
@Repository // Indica que esta interfaz es un componente de repositorio de Spring
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

}