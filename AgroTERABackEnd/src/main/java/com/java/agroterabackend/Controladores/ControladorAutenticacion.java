package com.java.agroterabackend.Controladores;

import com.java.agroterabackend.Payload.Requests.LoginRequest; // Importa LoginRequest
import com.java.agroterabackend.Payload.Responses.JWTRespuestas ; // Importa JwtResponse
import com.java.agroterabackend.Seguridad.Detalles.DetallesUsuario; // Importa UsuarioDetails
import com.java.agroterabackend.Seguridad.JWT.Jwtutil; // Importa JwtUtil
import org.springframework.beans.factory.annotation.Autowired; // Importa Autowired
import org.springframework.http.ResponseEntity; // Importa ResponseEntity
import org.springframework.security.authentication.AuthenticationManager; // Importa AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Importa UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication; // Importa Authentication
import org.springframework.security.core.context.SecurityContextHolder; // Importa SecurityContextHolder
import org.springframework.web.bind.annotation.*; // Importa todas las anotaciones de REST
import jakarta.validation.Valid; // Importa Valid

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/authenticate") // Ruta base para el endpoint de autenticación
public class ControladorAutenticacion {

    @Autowired // Inyecta el AuthenticationManager
    private AuthenticationManager authenticationManager;

    @Autowired // Inyecta el JwtUtil
    private Jwtutil jwtUtil;

    @PostMapping // Endpoint para el login
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Realiza la autenticación usando el AuthenticationManager
        // Esto invoca a UsuarioDetailsService y PasswordEncoder
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Si la autenticación es exitosa, establece el objeto de autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Genera el token JWT
        String jwt = jwtUtil.generateToken((DetallesUsuario) authentication.getPrincipal());

        // Obtiene los detalles del usuario autenticado
        DetallesUsuario userDetails = (DetallesUsuario) authentication.getPrincipal();

        // Construye la respuesta JWT
        return ResponseEntity.ok(new JWTRespuestas(
                jwt,
                "Bearer",
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getRoles() // ¡Aquí está el sexto valor, la lista de roles!
        ));
    }
}
