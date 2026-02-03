package com.ms_auth.auth.security;

import com.ms_auth.auth.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private Usuario usuario;

    public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convertimos el ROL del usuario a un formato que Spring entienda
        return Collections.singleton(new SimpleGrantedAuthority(usuario.getRol()));
    }

    @Override
    public String getPassword() { return usuario.getPassword(); }

    @Override
    public String getUsername() { return usuario.getEmail(); }

    // Métodos de estado de cuenta (ponemos todos true para que no bloqueen)
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
    
    public Usuario getUsuario() { return usuario; }
}