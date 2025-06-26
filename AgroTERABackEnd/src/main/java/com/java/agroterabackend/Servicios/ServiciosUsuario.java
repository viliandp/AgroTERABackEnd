package com.java.agroterabackend.Servicios;

import com.java.agroterabackend.Modelos.Usuario; // Importa la entidad Usuario
import com.java.agroterabackend.Repositorios.RepositorioUsuarios; // Importa el repositorio de Usuario
import org.springframework.beans.factory.annotation.Autowired; // Importa Autowired
import org.springframework.security.crypto.password.PasswordEncoder; // Importa PasswordEncoder
import org.springframework.stereotype.Service; // Importa Service

import java.util.List; // Importa List
import java.util.Optional; // Importa Optional

@Service // Indica que esta clase es un componente de servicio de Spring
public class ServiciosUsuario {

    private final RepositorioUsuarios usuarioRepository;
    private final PasswordEncoder passwordEncoder; // Inyectamos PasswordEncoder

    @Autowired // Inyecta el repositorio y el codificador de contraseñas
    public ServiciosUsuario(RepositorioUsuarios usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método para registrar un nuevo usuario
    public Usuario registrarUsuario(Usuario usuario) {
        // Antes de guardar, ciframos la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    // Método para obtener todos los usuarios
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Método para obtener un usuario por ID
    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    // Método para obtener un usuario por nombre de usuario
    public Optional<Usuario> getUsuarioByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    // Método para actualizar un usuario
    public Usuario updateUsuario(Long id, Usuario usuarioDetails) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario existingUsuario = optionalUsuario.get();
            existingUsuario.setNombreUsuario(usuarioDetails.getNombreUsuario());
            existingUsuario.setEmail(usuarioDetails.getEmail());
            // Si la contraseña se actualiza, también debe ser cifrada
            if (usuarioDetails.getPassword() != null && !usuarioDetails.getPassword().isEmpty()) {
                existingUsuario.setPassword(passwordEncoder.encode(usuarioDetails.getPassword()));
            }
            return usuarioRepository.save(existingUsuario);
        } else {
            // Manejar el caso de usuario no encontrado, por ejemplo, lanzando una excepción
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
    }

    // Método para eliminar un usuario
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Método para verificar si un nombre de usuario ya existe
    public boolean existsByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }

    // Método para verificar si un correo electrónico ya existe
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
