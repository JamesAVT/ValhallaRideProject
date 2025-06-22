package com.valhallaride.valhallaride.controller;

import com.valhallaride.valhallaride.model.ProductoOrden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.valhallaride.valhallaride.service.ProductoOrdenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/producto-orden")
@Tag(name = "Productos Ordenes", description = "Operaciones relacionadas con Productos Ordenes")

public class ProductoOrdenController {

    @Autowired
    private ProductoOrdenService productoOrdenService;

    @GetMapping
    @Operation(summary = "Listar productos en ordenes", description = "Obtiene una lista de todas las relaciones entre productos y ordenes")
    public ResponseEntity<List<ProductoOrden>> listarTodos() {
        List<ProductoOrden> lista = productoOrdenService.findAll(); // Llama a service para obtener todos los registros de ProductoOrden
        if (lista.isEmpty()) // Si la lista está vacía
            return ResponseEntity.noContent().build(); // Responde con 204 No Content
        return ResponseEntity.ok(lista); // Y si hay datos, devuelve la lista con 200 OK
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto orden por ID", description = "Obtiene una relacion entre un producto y una orden, usando su ID")
    public ResponseEntity<ProductoOrden> buscarPorId(@PathVariable Integer id) {
        ProductoOrden encontrado = productoOrdenService.findById(id); // Llama a service para buscar el objeto ProductoOrden con la ID
        return (encontrado != null) ? ResponseEntity.ok(encontrado) : ResponseEntity.notFound().build(); // Si el objeto fue encontrado, devuelve 200 OK con el objeto ProductoOrden 
                                                                                                        // Si el objeto no se encontró, devuelve 404 Not Found
    }

    @GetMapping("/por-orden/{idOrden}")
    @Operation(summary = "Listar ProductoOrden por ID de la orden", description = "Muestra todas las relaciones producto-orden que pertenecen a una orden especifica por su ID")
    public ResponseEntity<List<ProductoOrden>> buscarPorOrden(@PathVariable Integer idOrden) {
        List<ProductoOrden> lista = productoOrdenService.findByOrden(idOrden); // Llama a service para obtener todos los productos asociados a la orden con la ID
        if (lista.isEmpty()) // Si la lista está vacía (no hay productos asociados a esa orden)
            return ResponseEntity.noContent().build(); // Devuelve 204 Not Content
        return ResponseEntity.ok(lista); // Y si encontró, devuelve la lista con 200 OK
    }

    @GetMapping("/por-producto/{idProducto}")
    @Operation(summary = "Listar ProductoOrden por ID del producto", description = "Muestra todas las relaciones producto-orden que contiene el producto especificado por su ID")
    public ResponseEntity<List<ProductoOrden>> buscarPorProducto(@PathVariable Integer idProducto) {
        List<ProductoOrden> lista = productoOrdenService.findByProducto(idProducto); // Llama a service para buscar todos los registros de ProductoOrden asociados a ese producto
        if (lista.isEmpty()) // Si la lista está vacía (no hay registros asociados a ese producto)
            return ResponseEntity.noContent().build(); // Responde con 204 No Content
        return ResponseEntity.ok(lista); // Y si enceuntra registros, devuelve la lista con 200 OK
    }

    @GetMapping("/ordenado-por-fecha")
    @Operation(summary = "Listar ProductoOrden ordenados por fecha", description = "Obtiene todas las relaciones producto-orden ordenadas por su fecha de orden")
    public ResponseEntity<List<ProductoOrden>> listarOrdenadoPorFecha() {
        List<ProductoOrden> lista = productoOrdenService.findAllOrderByFechaHoraDesc(); // Llama a service para obtener todos los ProductoOrden ordenados por fecha
        if (lista.isEmpty()) // Si la lista está vacía
            return ResponseEntity.noContent().build(); // Responde con 204 No Content
        return ResponseEntity.ok(lista); // Y si hay registros, devuelve la lista con 200 OK
    }

    // Query 3 - 4 tablas
    @GetMapping("/producto-usuario")
    public List<Object[]> getProductoOrdenConProductoYUsuario() {
        return productoOrdenService.listarProductoOrdenConProductoYUsuario();
    }

    @PostMapping
    @Operation(summary = "Crear relación producto-orden", description = "Crea una nueva relacion entre un producto y una orden")
    public ResponseEntity<ProductoOrden> registrar(@RequestBody ProductoOrden productoOrden) {
        ProductoOrden guardado = productoOrdenService.save(productoOrden); // Llama a service para guardar el objeto ProductoOrden
        return ResponseEntity.ok(guardado); // Devuelve una respuesta 200 OK
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar ProductoOrden por su ID", description = "Actualiza los datos de una relacion ya existente de un producto y una orden")
    public ResponseEntity<ProductoOrden> actualizar(@PathVariable Integer id, @RequestBody ProductoOrden productoOrden) {
        ProductoOrden existente = productoOrdenService.findById(id); // Busca el ProductoOrden por su ID
        if (existente == null) { // Si no existe
            return ResponseEntity.notFound().build(); // Responde con 404 Not Found
        }
        // Actualiza los campos del objeto existente con los valores nuevos 
        existente.setCantidad(productoOrden.getCantidad());
        existente.setFechaHora(productoOrden.getFechaHora());
        existente.setOrden(productoOrden.getOrden());
        existente.setPrecioProducto(productoOrden.getPrecioProducto());
        existente.setProducto(productoOrden.getProducto());

        ProductoOrden actualizado = productoOrdenService.save(existente); // Guarda el objeto actualizado
        return ResponseEntity.ok(actualizado); // Devuelve una respuesta 200 OK con el objeto actualizado
        
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un ProductoOrden por su ID", description = "Permite actualizar parcialmente los datos de una relación producto-orden existente")
    public ResponseEntity<ProductoOrden> patchProductoOrden(@PathVariable Integer id, @RequestBody ProductoOrden partialProductoOrden) {
        ProductoOrden existente = productoOrdenService.findById(id); // Busca un ProductoOrden existente
        if (existente == null) { // Si no existe
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found
        }

        // Chicos, esto actualiza solo si el campo enviado NO es null!!
        if (partialProductoOrden.getCantidad() != null) {
            existente.setCantidad(partialProductoOrden.getCantidad());
        }
        if (partialProductoOrden.getFechaHora() != null) {
            existente.setFechaHora(partialProductoOrden.getFechaHora());
        }
        if (partialProductoOrden.getOrden() != null) {
            existente.setOrden(partialProductoOrden.getOrden());
        }
        if (partialProductoOrden.getPrecioProducto() != null) {
            existente.setPrecioProducto(partialProductoOrden.getPrecioProducto());
        }
        if (partialProductoOrden.getProducto() != null) {
            existente.setProducto(partialProductoOrden.getProducto());
        }

        ProductoOrden actualizado = productoOrdenService.save(existente); // Guarda el objeto actualizado
        return ResponseEntity.ok(actualizado); // Devuelve 200 OK con el objeto modificado
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar ProductoOrden por su ID", description = "Elimina la relación entre un producto y una orden existente")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) { // Método que responde a DELETE, recibiendo el ID de ProductoOrden
        productoOrdenService.delete(id); // Llama a service para eliminar el ProductoOrden con ese ID
        return ResponseEntity.noContent().build(); // Devuelve respuesta 204 No Content, indicando que la eliminación fue existosa
    }
}
