package com.java.agroterabackend.Seguridad.Configuracion;


import com.java.agroterabackend.Seguridad.Detalles.DetallesUsuarioServicio; // Importa tu UserDetailsService
import com.java.agroterabackend.Seguridad.JWT.FiltroRequestJWT; // Importa tu filtro JWT
import org.springframework.beans.factory.annotation.Autowired; // Importa Autowired
import org.springframework.context.annotation.Bean; // Importa Bean
import org.springframework.context.annotation.Configuration; // Importa Configuration
import org.springframework.security.authentication.AuthenticationManager; // Importa AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Importa DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Importa AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Importa EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Importa HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Importa EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy; // Importa SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importa BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder; // Importa PasswordEncoder
import org.springframework.security.web.SecurityFilterChain; // Importa SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Importa UsernamePasswordAuthenticationFilter

@Configuration // Marca esta clase como una clase de configuración de Spring
@EnableWebSecurity // Habilita la seguridad web de Spring
@EnableMethodSecurity // Habilita la seguridad a nivel de método (ej. @PreAuthorize)
public class ConfiguracionSeguridad {

    @Autowired // Inyecta tu UsuarioDetailsService
    private DetallesUsuarioServicio usuarioDetailsService;

    @Autowired // Inyecta tu filtro JWT
    private FiltroRequestJWT jwtRequestFilter;

    // Define un bean para el codificador de contraseñas (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt es un algoritmo de hash de contraseñas fuerte y recomendado
        return new BCryptPasswordEncoder();
    }

    // Configura el proveedor de autenticación que usará tu UserDetailsService y el PasswordEncoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioDetailsService); // Le dice a Spring cómo cargar usuarios
        authProvider.setPasswordEncoder(passwordEncoder()); // Le dice a Spring cómo codificar contraseñas
        return authProvider;
    }

    // Define el AuthenticationManager, necesario para autenticar usuarios
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Configura la cadena de filtros de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF (típico para APIs REST sin sesiones basadas en cookies)
                .authorizeHttpRequests(authorize -> authorize
                        // Permite el acceso sin autenticación a los endpoints de registro y autenticación
                        .requestMatchers("/api/usuarios/registrar", "/authenticate").permitAll()
                        // Permite el acceso sin autenticación a los endpoints de productos (ajusta si quieres protegerlos)
                        .requestMatchers("/api/productos/**").permitAll()
                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        // Configura Spring Security para no crear sesiones (stateless)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Añade tu filtro JWT antes del filtro de autenticación de usuario y contraseña de Spring
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

