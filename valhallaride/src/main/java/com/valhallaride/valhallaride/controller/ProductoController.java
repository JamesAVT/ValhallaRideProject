package com.valhallaride.valhallaride.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.valhallaride.valhallaride.model.Producto;
import com.valhallaride.valhallaride.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con Productos")

public class ProductoController {

    @Autowired
    private ProductoService productoService;

    private void limpiarRelaciones(Producto p) { // Método que recibe un objeto producto y se encarga de limpiar las referencias dentro del producto para evitar bucles
        if (p.getCategoria() != null) { // Si el producto tiene asignada una categoria
            p.getCategoria().setProductos(null); // Accede a esa categoria y pone null a la lista de productos relacionados
        }
        if (p.getTienda() != null) { // Y aquí lo mismo, limpia productos de la tienda para evitar bucles
            p.getTienda().setProductos(null);
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Obtiene una lista de todos los productos")
    public ResponseEntity<List<Producto>> listar() { // Método que devuelve un ResponseEntity con una lista de productos
        List<Producto> productos = productoService.findAll(); // Llama a service para obtener todos los productos 
        if (productos.isEmpty()) { // Si la lista está vacía 
            return ResponseEntity.noContent().build(); // Responde con código 204 No Content para indicar que no hay productos
        }

        for (Producto p : productos) { // Recorre cada producto para limpiar las referencias
            if (p.getCategoria() != null) { // Si el producto tiene una categoría
                p.getCategoria().setProductos(null); // Limpia la lista de productos dentro de esa categoría
            }
            if (p.getTienda() != null) { // Si el producto tiene una tienda
                p.getTienda().setProductos(null); // Limpia la lista de productos dentro de esa tienda
            }
        }
        return ResponseEntity.ok(productos); // Responde con 200 OK y envía la lista de productos 
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener productos por ID", description = "Obtiene un producto por su ID")
    public ResponseEntity<Producto> buscar(@PathVariable Integer id) { // Método que recibe el ID del producto desde la URL
        try {
            Producto producto = productoService.findById(id); // Intenta obtener el producto con ese ID desde la base de datos mediante el service

            if (producto.getCategoria() != null) {
                producto.getCategoria().setProductos(null);
            }                                   // Si el producto tiene una categoría o una tienda asignada, limpia las listas de productos, evitando bucles
            if (producto.getTienda() != null) {
                producto.getTienda().setProductos(null);
            }
            return ResponseEntity.ok(producto); // Devuelve una respuesta 200 OK
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // Y si ocurre cualquier excepción, responde con 404 Not Found.
        }
    }

    // Buscar productos cuyo nombre contenga cierta palabra (insensible a
    // mayúsculas)
    @GetMapping("/buscar-por-nombre")
    @Operation(summary = "Obtener productos por nombre", description = "Obtiene una lista de productos cuyo nombre coincida con el valor proporcionado")
    public ResponseEntity<List<Producto>> buscarPorNombre( // Método que recibe un parametro "nombre" desde la URL
            @org.springframework.web.bind.annotation.RequestParam String nombre) { // Parámetro de consulta
        List<Producto> productos = productoService.buscarPorNombre(nombre); // Llama a service para buscar productos cuyo nombre coincida con el parámetro recibido
        if (productos.isEmpty()) // Si no hay productos que coincidad 
            return ResponseEntity.noContent().build(); // Responde con código 204 No Content
        productos.forEach(this::limpiarRelaciones); // Para cada producto encontrado, llama al método limpiarRelaciones, para evitar bucles
        return ResponseEntity.ok(productos); // Devuelve 200 OK con la lista de productos
    }

    // Buscar productos por nombre exacto y categoría
    @GetMapping("/buscar-por-nombre-categoria")
    @Operation(summary = "Obtener productos por nombre y categoria", description = "Obtiene una lista de productos que coinciden con un nombre dentro de una categoria especifica ")
    public ResponseEntity<List<Producto>> buscarPorNombreYCategoria(
            @org.springframework.web.bind.annotation.RequestParam String nombre,
            @org.springframework.web.bind.annotation.RequestParam Integer idCategoria) {
        List<Producto> productos = productoService.buscarPorNombreYCategoria(nombre, idCategoria); // Lla,a a service para buscar productos que coincida con el nombre y la ID
        if (productos.isEmpty()) // Si no hay productos que cumplan con ambas especificaciones
            return ResponseEntity.noContent().build(); // Responde con 204 No Contet
        productos.forEach(this::limpiarRelaciones); // Y para cada producto encontrado, limpia las referencias para evitar bucles
        return ResponseEntity.ok(productos); // Devuelve 200 OK junto con la lista de productos 
    }

    // Buscar productos dentro de un rango de precios
    @GetMapping("/buscar-rango-precio")
    @Operation(summary = "Obtener productos por rango de precio", description = "Obtiene una lista de productos cuyo precio este dentro de un rango especificado")
    public ResponseEntity<List<Producto>> buscarPorRangoPrecio(
            @org.springframework.web.bind.annotation.RequestParam Integer min, // Precio mínimo
            @org.springframework.web.bind.annotation.RequestParam Integer max) { // Precio máximo
        List<Producto> productos = productoService.buscarPorRangoDePrecio(min, max); // Llama a service para obtener todos los productos que esten entre el precio mínimo y máximo y los devuelve
        if (productos.isEmpty()) // Si no hay productos dentro del rango
            return ResponseEntity.noContent().build(); // Responde con 204 No Content
        productos.forEach(this::limpiarRelaciones); // Para cada producto, limpia referencias para evitar bucles
        return ResponseEntity.ok(productos); // Devuelve 200 OK con la lista de productos
    }

    // Buscar productos de una tienda ordenados por precio
    @GetMapping("/buscar-por-tienda-precio-desc")
    @Operation(summary = "Obtener productos de una tienda ordenados por precio descendente", description = "Obtiene los productos de la tienda, ordenados de mayor a menor segun el precio")
    public ResponseEntity<List<Producto>> buscarPorTiendaOrdenadoPorPrecio(
            @org.springframework.web.bind.annotation.RequestParam Integer idTienda) {
        List<Producto> productos = productoService.buscarPorTiendaOrdenadoPorPrecio(idTienda); // Llama a service para obtener todos los productos que esten en la teinda, ordenados por precio
        if (productos.isEmpty()) // Si productos está vacio
            return ResponseEntity.noContent().build(); // Responde con 204 No Content
        productos.forEach(this::limpiarRelaciones); // Limpia las referencias que pueden causar bucles
        return ResponseEntity.ok(productos); // Devuelve 200 OK 
    }

    // Buscar productos con stock menor a cierto valor
    @GetMapping("/buscar-stock-bajo")
    @Operation(summary = "Obtener productos con bajo stock", description = "Obtiene una lista de productos cuyo stock es bajo")
    public ResponseEntity<List<Producto>> buscarConStockBajo(
            @org.springframework.web.bind.annotation.RequestParam Integer stock) {
        List<Producto> productos = productoService.buscarConStockBajo(stock); // Llama a service para buscar esos productos 
        if (productos.isEmpty()) // Si no hay productos con stock bajo
            return ResponseEntity.noContent().build(); // Responde con 204 No Content
        productos.forEach(this::limpiarRelaciones);
        return ResponseEntity.ok(productos); // Limpia las relaciones (categoria, tienda) para evitar bucles
    }

    // Query 1 - 3 tablas
    @GetMapping("/productos-detallados")
    public ResponseEntity<List<Map<String, Object>>> obtenerProductosDetallados() {
        return ResponseEntity.ok(productoService.listarProductosConNombres());
    }

    @PostMapping
    @Operation(summary = "Crear Producto", description = "Crea un nuevo producto")
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto) { // Método que recibe un producto en formato JSON desde el @RequestBody y lo convierte a un objeto Producto
        Producto productoNuevo = productoService.save(producto); // Llama a service para guardar el producto
        return ResponseEntity.status(HttpStatus.CREATED).body(productoNuevo); // Devuelve 201 Created
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Producto por su ID", description = "Actualiza todos los datos de un producto ya existente")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        try {
            Producto productoYaExistente = productoService.findById(id); // Busca el producto existente en la base de datos usando el ID
            if (productoYaExistente == null) { // Si no se encuentra el producto
                return ResponseEntity.notFound().build(); // Responde con 404 Not Found
            }
            // Aquí se actualizan uno por uno los campos del producto existente con los valores nuevos en el RequestBody
            productoYaExistente.setNombreProducto(producto.getNombreProducto());
            productoYaExistente.setDescripcionProducto(producto.getDescripcionProducto());
            productoYaExistente.setPrecioProducto(producto.getPrecioProducto());
            productoYaExistente.setStockProducto(producto.getStockProducto());
            productoYaExistente.setCategoria(producto.getCategoria());
            productoYaExistente.setTienda(producto.getTienda());

            Producto productoActualizado = productoService.save(productoYaExistente); // Guarda el producto actualizado
            return ResponseEntity.ok(productoActualizado); // Devuelve 200 OK con el producto actualizado
        } catch (Exception e) { // Si llegara a ocurrir una excepción
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un Producto por ID", description = "Actualiza parcialmente los datos de un producto existente")
    public ResponseEntity<Producto> patchProducto(@PathVariable Integer id, @RequestBody Producto partialProducto) {
        try {
            Producto updatedProducto = productoService.patchProducto(id, partialProducto); // Llama a service para hacer la actualización 
            // Limpia referencias en la categoría y tienda, para evitar bucles 
            if (updatedProducto.getCategoria() != null) {
                updatedProducto.getCategoria().setProductos(null);
            }
            if (updatedProducto.getTienda() != null) {
                updatedProducto.getTienda().setProductos(null);
            }

            return ResponseEntity.ok(updatedProducto); // Devuele 200 OK con el producto actualizado
        } catch (RuntimeException e) { // Si ocurre una excepción
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Producto por su ID", description = "Elimina un producto existente")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            productoService.delete(id); // Llama a service para eliminar el producto
            return ResponseEntity.noContent().build(); // Si la eliminación fue exitosa, responde con 204 Not Found
        } catch (Exception e) { // Si ocurre una excepción
            return ResponseEntity.notFound().build(); // Responde con 404 Not Found
        }
    }
}
