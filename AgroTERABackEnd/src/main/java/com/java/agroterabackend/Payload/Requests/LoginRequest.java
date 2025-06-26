package com.java.agroterabackend.Payload.Requests;

import lombok.Data; // Anotación de Lombok para getters, setters, etc.
import jakarta.validation.constraints.NotBlank; // Importa NotBlank para validación

@Data // Genera automáticamente getters y setters
public class LoginRequest {
    @NotBlank(message = "El nombre de usuario no puede estar vacío") // Valida que el campo no esté en blanco
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía") // Valida que el campo no esté en blanco
    private String password;
}
