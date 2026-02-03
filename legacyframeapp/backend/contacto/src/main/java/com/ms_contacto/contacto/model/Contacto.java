// ...existing code...
package com.ms_contacto.contacto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "contactos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "nombre requerido")
    @Size(max = 50)
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "email requerido")
    @Email(message = "email inválido")
    @Size(max = 50)
    @Column(nullable = false, length = 150)
    private String email;

    @NotBlank(message = "mensaje requerido")
    @Size(max = 300)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;

    @PrePersist
    protected void onCreate() {
        if (fechaEnvio == null) {
            fechaEnvio = LocalDateTime.now();
        }
    }
}