package com.ms_auth.auth.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateProfileDto {
    @Pattern(regexp = "^[\\p{L} .'-]{2,}$", message = "El nombre solo puede contener letras, espacios y caracteres válidos (incluye Ñ)")
    private String nombre;
    @Pattern(regexp = "^[\\p{L} .'-]{2,}$", message = "El apellido solo puede contener letras, espacios y caracteres válidos (incluye Ñ)")
    private String apellido;
    private String telefono;
    private String direccion;
    
    // Opcionales: Si vienen vacíos, no cambiamos la contraseña
    private String password;
    private String confirmPassword;
}