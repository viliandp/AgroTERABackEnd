package com.java.agroterabackend.Seguridad.Detalles;

import com.java.agroterabackend.Modelos.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// This class adapts your Usuario entity to Spring Security's UserDetails interface
public class DetallesUsuario implements UserDetails {

    private Long id;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    // Constructor that takes your Usuario entity
    public DetallesUsuario(Usuario usuario) {
        this.id = usuario.getId();
        this.username = usuario.getNombreUsuario();
        this.email = usuario.getEmail();
        this.password = usuario.getPassword();
        // For now, we assign a fixed role (e.g., "ROLE_USER").
        // In a real application, roles would be loaded from the database or business logic.
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // New Getter for the ID
    public Long getId() {
        return id;
    }

    // New Getter for the Email
    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // New method to get roles as List<String>
    public List<String> getRoles() {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority) // Gets the role string (e.g., "ROLE_USER")
                .collect(Collectors.toList()); // Collects into a list
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Keep as true unless you implement account expiration logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Keep as true unless you implement account locking logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Keep as true unless you implement credential expiration logic
    }

    @Override
    public boolean isEnabled() {
        return true; // Keep as true unless you implement account enabling logic
    }
}
