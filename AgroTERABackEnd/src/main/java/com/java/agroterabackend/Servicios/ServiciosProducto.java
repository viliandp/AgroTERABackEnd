package com.java.agroterabackend.Servicios;

import com.java.agroterabackend.Modelos.Productos;
import com.java.agroterabackend.Repositorios.RepositorioProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un componente de servicio de Spring
public class ServiciosProducto {

    private final RepositorioProductos productoRepository;

    @Autowired // Inyecta el repositorio automáticamente
    public ServiciosProducto(RepositorioProductos productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Obtener todos los productos
    public List<Productos> getAllProductos() {
        return productoRepository.findAll();
    }

    // Obtener un producto por ID
    public Optional<Productos> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    // Guardar un nuevo producto o actualizar uno existente
    public Productos saveProducto(Productos productos) {
        return productoRepository.save(productos);
    }

    // Eliminar un producto por ID
    public void deleteProducto(Long id) {
        productoRepository.deleteById(id);
    }

    // Buscar producto por nombre (ejemplo de método personalizado del repositorio)
    public Productos getProductoByNombre(String nombre) {
        return productoRepository.findByNombre(nombre);
    }
}