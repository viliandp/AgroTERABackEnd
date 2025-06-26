package com.java.agroterabackend.Repositorios;

import com.java.agroterabackend.Modelos.Productos; // Importa tu clase de entidad
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// JpaRepository toma dos tipos: la entidad con la que trabaja y el tipo de su ID.
@Repository // Opcional, pero buena práctica para indicar que es un componente de Spring
public interface RepositorioProductos extends JpaRepository<Productos, Long> {

    Productos findByNombre(String nombre);

    List<Productos> findByPrecioLessThan(double precio);
}
