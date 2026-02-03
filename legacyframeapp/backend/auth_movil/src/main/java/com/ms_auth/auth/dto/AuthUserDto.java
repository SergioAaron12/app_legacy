package com.ms_auth.auth.dto;

import jakarta.validation.constraints.*;

public class AuthUserDto {
    
    @NotBlank(message = "El nombre es requerido")
    @Pattern(regexp = "^[\\p{L} .'-]{2,}$", message = "El nombre solo puede contener letras, espacios y caracteres válidos (incluye Ñ)")
    private String nombre;
    
    @Pattern(regexp = "^[\\p{L} .'-]{2,}$", message = "El apellido solo puede contener letras, espacios y caracteres válidos (incluye Ñ)")
    private String apellido;

    @NotBlank(message = "El RUT es requerido")
    private String rut;

    @NotBlank(message = "El DV es requerido")
    private String dv;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    private String email;
    
    @NotBlank(message = "El teléfono es requerido")
    private String telefono;
    
    @NotBlank(message = "La contraseña es requerida")
    private String password;
    
    @NotBlank(message = "Debe confirmar la contraseña")
    private String confirmPassword;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getDv() { return dv; }
    public void setDv(String dv) { this.dv = dv; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}