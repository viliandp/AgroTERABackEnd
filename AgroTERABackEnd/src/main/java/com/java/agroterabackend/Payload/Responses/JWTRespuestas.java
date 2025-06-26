package com.java.agroterabackend.Payload.Responses;

import lombok.AllArgsConstructor; // Anotación de Lombok para constructor con todos los argumentos
import lombok.Data; // Anotación de Lombok para getters, setters, etc.

import java.util.List;

@Data // Genera automáticamente getters y setters
@AllArgsConstructor // Genera un constructor con todos los argumentos
public class JWTRespuestas {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles; // Nuevo campo para la lista de roles
}
