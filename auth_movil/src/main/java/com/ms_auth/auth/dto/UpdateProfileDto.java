package com.ms_auth.auth.dto;
import lombok.Data;

@Data
public class UpdateProfileDto {
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    
    // Opcionales: Si vienen vacíos, no cambiamos la contraseña
    private String password;
    private String confirmPassword;
}