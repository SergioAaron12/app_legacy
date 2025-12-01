package com.ms_auth.auth.dto;

import lombok.Data;

@Data
public class LoginDto {
    // Puede autenticarse por email (email + password) o por RUT (rut [+ dv] + password)
    private String email;
    private String rut;
    private String dv; // opcional, puede venir en el campo `rut` separado por '-'
    private String password;
}