package com.java.agroterabackend.Controladores;

import com.java.agroterabackend.Modelos.Productos;
import com.java.agroterabackend.Servicios.ServiciosProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Declara que esta clase es un controlador REST
@RequestMapping("/api/productos") // Define el path base para todos los endpoints de este controlador
public class ControladorProductos {

    private final ServiciosProducto productoService;

    @Autowired
    public ControladorProductos(ServiciosProducto productoService) {
        this.productoService = productoService;
    }

    // Endpoint para obtener todos los productos
    // GET http://localhost:8080/api/productos
    @GetMapping
    public ResponseEntity<List<Productos>> getAllProductos() {
        List<Productos> productos = productoService.getAllProductos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    // Endpoint para obtener un producto por su ID
    // GET http://localhost:8080/api/productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Productos> getProductoById(@PathVariable Long id) {
        Optional<Productos> producto = productoService.getProductoById(id);
        return producto.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para crear un nuevo producto
    // POST http://localhost:8080/api/productos
    @PostMapping
    public ResponseEntity<Productos> createProducto(@RequestBody Productos productos) {
        Productos savedProductos = productoService.saveProducto(productos);
        return new ResponseEntity<>(savedProductos, HttpStatus.CREATED);
    }

    // Endpoint para actualizar un producto existente
    // PUT http://localhost:8080/api/productos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Productos> updateProducto(@PathVariable Long id, @RequestBody Productos productosDetails) {
        Optional<Productos> productoOptional = productoService.getProductoById(id);
        if (productoOptional.isPresent()) {
            Productos existingProductos = productoOptional.get();
            existingProductos.setNombre(productosDetails.getNombre());
            existingProductos.setPrecio(productosDetails.getPrecio());
            existingProductos.setStock(productosDetails.getStock());
            Productos updatedProductos = productoService.saveProducto(existingProductos);
            return new ResponseEntity<>(updatedProductos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para eliminar un producto
    // DELETE http://localhost:8080/api/productos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoService.getProductoById(id).isPresent()) {
            productoService.deleteProducto(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
