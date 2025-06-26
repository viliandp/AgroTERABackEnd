package com.java.agroterabackend.Seguridad.Detalles;

import com.java.agroterabackend.Modelos.Usuario; // Importa tu entidad Usuario
import com.java.agroterabackend.Repositorios.RepositorioUsuarios; // Importa tu repositorio de Usuario
import org.springframework.beans.factory.annotation.Autowired; // Importa Autowired
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails
import org.springframework.security.core.userdetails.UserDetailsService; // Importa UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importa UsernameNotFoundException
import org.springframework.stereotype.Service; // Importa Service

import java.util.Optional; // Importa Optional

@Service // Marca esta clase como un servicio de Spring
public class DetallesUsuarioServicio implements UserDetailsService {

    @Autowired // Inyecta el repositorio de Usuario
    private RepositorioUsuarios usuarioRepository;

    @Override
    // Este método es invocado por Spring Security para cargar los detalles de un usuario
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByNombreUsuario(username);
        // Si el usuario no se encuentra, lanza una excepción
        return usuarioOptional.map(DetallesUsuario::new) // Si se encuentra, crea un UsuarioDetails
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}
