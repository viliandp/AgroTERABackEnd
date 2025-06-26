package com.java.agroterabackend.Controladores;

import com.java.agroterabackend.Modelos.Usuario; // Importa la entidad Usuario
import com.java.agroterabackend.Servicios.ServiciosUsuario; // Importa el servicio de Usuario
import org.springframework.beans.factory.annotation.Autowired; // Importa Autowired
import org.springframework.http.HttpStatus; // Importa HttpStatus
import org.springframework.http.ResponseEntity; // Importa ResponseEntity
import org.springframework.web.bind.annotation.*; // Importa todas las anotaciones de REST
import jakarta.validation.Valid; // Importa Valid para validación

import java.util.List; // Importa List
import java.util.Optional; // Importa Optional

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/usuarios") // Define la ruta base para los endpoints de usuario
public class ControladorUsuarios {

    private final ServiciosUsuario usuarioService;

    @Autowired // Inyecta el servicio de usuario
    public  ControladorUsuarios(ServiciosUsuario usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint para registrar un nuevo usuario (Crear)
    // POST http://localhost:8080/api/usuarios/registrar
    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody Usuario usuario) {
        // Validar si el nombre de usuario ya existe
        if (usuarioService.existsByNombreUsuario(usuario.getNombreUsuario())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 Conflict
        }
        Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED); // 201 Created
    }

    // Endpoint para obtener todos los usuarios (Leer)
    // GET http://localhost:8080/api/usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK); // 200 OK
    }

    // Endpoint para obtener un usuario por ID (Leer)
    // GET http://localhost:8080/api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        return usuario.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // 200 OK
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
    }

    // Endpoint para actualizar un usuario (Actualizar)
    // PUT http://localhost:8080/api/usuarios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuarioDetails) {
        try {
            Usuario updatedUsuario = usuarioService.updateUsuario(id, usuarioDetails);
            return new ResponseEntity<>(updatedUsuario, HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // Endpoint para eliminar un usuario (Borrar)
    // DELETE http://localhost:8080/api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        if (usuarioService.getUsuarioById(id).isPresent()) {
            usuarioService.deleteUsuario(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }
}

