package com.ms_auth.auth.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
}