package com.java.agroterabackend.Modelos;

import jakarta.persistence.*; // Importa todas las anotaciones de JPA
import lombok.Data; // Anotación de Lombok para getters, setters, toString, equals y hashCode
import lombok.NoArgsConstructor; // Anotación de Lombok para constructor sin argumentos
import lombok.AllArgsConstructor; // Anotación de Lombok para constructor con todos los argumentos

@Entity // Marca esta clase como una entidad JPA
@Table(name = "Usuarios") // Especifica el nombre de la tabla en la base de datos
@Data // Genera automáticamente getters, setters, toString, equals y hashCode con Lombok
@NoArgsConstructor // Genera un constructor sin argumentos con Lombok
@AllArgsConstructor // Genera un constructor con todos los argumentos con Lombok
public class Usuario {

    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la estrategia de generación automática de ID
    private Long id;

    @Column(nullable = false, unique = true) // Nombre de usuario no puede ser nulo y debe ser único
    private String nombreUsuario;

    @Column(nullable = false) // Correo electrónico no puede ser nulo
    private String email;

    @Column(nullable = false) // Contraseña no puede ser nula
    private String password; // ¡Esta contraseña será almacenada cifrada!

}

