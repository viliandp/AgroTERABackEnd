package com.java.agroterabackend.Repositorios;


import com.java.agroterabackend.Modelos.Usuario; // Importa la entidad Usuario
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository
import org.springframework.stereotype.Repository; // Opcional, pero buena práctica

import java.util.Optional; // Importa Optional

@Repository // Indica que esta interfaz es un componente de repositorio de Spring
public interface RepositorioUsuarios extends JpaRepository<Usuario, Long> {

    // Método para encontrar un usuario por su nombre de usuario.
    // Spring Data JPA lo implementará automáticamente.
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    // Método para encontrar un usuario por su correo electrónico.
    Optional<Usuario> findByEmail(String email);

    // Método para verificar si un nombre de usuario ya existe
    boolean existsByNombreUsuario(String nombreUsuario);

    // Método para verificar si un correo electrónico ya existe
    boolean existsByEmail(String email);
}
