package com.java.agroterabackend.Seguridad.JWT;

import com.java.agroterabackend.Seguridad.Detalles.DetallesUsuarioServicio; // Importa tu UserDetailsService
import jakarta.servlet.FilterChain; // Importa FilterChain
import jakarta.servlet.ServletException; // Importa ServletException
import jakarta.servlet.http.HttpServletRequest; // Importa HttpServletRequest
import jakarta.servlet.http.HttpServletResponse; // Importa HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired; // Importa Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Importa UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder; // Importa SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Importa WebAuthenticationDetailsSource
import org.springframework.stereotype.Component; // Importa Component
import org.springframework.web.filter.OncePerRequestFilter; // Importa OncePerRequestFilter

import java.io.IOException; // Importa IOException

@Component // Marca esta clase como un componente de Spring
public class FiltroRequestJWT extends OncePerRequestFilter {

    @Autowired // Inyecta el JwtUtil para manejar los tokens
    private Jwtutil jwtUtil;

    @Autowired // Inyecta tu UsuarioDetailsService para cargar los detalles del usuario
    private DetallesUsuarioServicio usuarioDetailsService;

    @Override
    // Este método se ejecuta por cada solicitud HTTP
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Intenta obtener el encabezado "Authorization"
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Verifica si el encabezado existe y comienza con "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extrae el token JWT (después de "Bearer ")
            try {
                username = jwtUtil.extractUsername(jwt); // Extrae el nombre de usuario del token
            } catch (Exception e) {
                // Si hay un error al extraer el usuario (ej. token inválido o expirado)
                logger.warn("JWT Token does not begin with Bearer String or is invalid", e);
            }
        }

        // Si se extrajo un nombre de usuario y no hay autenticación actual en el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carga los detalles del usuario usando tu UsuarioDetailsService
            UserDetails userDetails = this.usuarioDetailsService.loadUserByUsername(username);

            // Valida el token contra los detalles del usuario
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // Si el token es válido, crea un objeto de autenticación
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // Establece detalles adicionales de la autenticación, como la dirección IP y la sesión
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Establece el objeto de autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // Continúa la cadena de filtros
        filterChain.doFilter(request, response);
    }
}

