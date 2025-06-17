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

    private void limpiarRelaciones(Producto p) {
        if (p.getCategoria() != null) {
            p.getCategoria().setProductos(null);
        }
        if (p.getTienda() != null) {
            p.getTienda().setProductos(null);
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Obtiene una lista de todos los productos")
    public ResponseEntity<List<Producto>> listar() {
        List<Producto> productos = productoService.findAll();
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        for (Producto p : productos) {
            if (p.getCategoria() != null) {
                p.getCategoria().setProductos(null);
            }
            if (p.getTienda() != null) {
                p.getTienda().setProductos(null);
            }
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener productos por ID", description = "Obtiene un producto por su ID")
    public ResponseEntity<Producto> buscar(@PathVariable Integer id) {
        try {
            Producto producto = productoService.findById(id);

            if (producto.getCategoria() != null) {
                producto.getCategoria().setProductos(null);
            }
            if (producto.getTienda() != null) {
                producto.getTienda().setProductos(null);
            }
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar productos cuyo nombre contenga cierta palabra (insensible a
    // mayúsculas)
    @GetMapping("/buscar-por-nombre")
    @Operation(summary = "Obtener productos por nombre", description = "Obtiene una lista de productos cuyo nombre coincida con el valor proporcionado")
    public ResponseEntity<List<Producto>> buscarPorNombre(
            @org.springframework.web.bind.annotation.RequestParam String nombre) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        if (productos.isEmpty())
            return ResponseEntity.noContent().build();
        productos.forEach(this::limpiarRelaciones);
        return ResponseEntity.ok(productos);
    }

    // Buscar productos por nombre exacto y categoría
    @GetMapping("/buscar-por-nombre-categoria")
    @Operation(summary = "Obtener productos por nombre y categoria", description = "Obtiene una lista de productos que coinciden con un nombre dentro de una categoria especifica ")
    public ResponseEntity<List<Producto>> buscarPorNombreYCategoria(
            @org.springframework.web.bind.annotation.RequestParam String nombre,
            @org.springframework.web.bind.annotation.RequestParam Integer idCategoria) {
        List<Producto> productos = productoService.buscarPorNombreYCategoria(nombre, idCategoria);
        if (productos.isEmpty())
            return ResponseEntity.noContent().build();
        productos.forEach(this::limpiarRelaciones);
        return ResponseEntity.ok(productos);
    }

    // Buscar productos dentro de un rango de precios
    @GetMapping("/buscar-rango-precio")
    @Operation(summary = "Obtener productos por rango de precio", description = "Obtiene una lista de productos cuyo precio este dentro de un rango especificado")
    public ResponseEntity<List<Producto>> buscarPorRangoPrecio(
            @org.springframework.web.bind.annotation.RequestParam Integer min,
            @org.springframework.web.bind.annotation.RequestParam Integer max) {
        List<Producto> productos = productoService.buscarPorRangoDePrecio(min, max);
        if (productos.isEmpty())
            return ResponseEntity.noContent().build();
        productos.forEach(this::limpiarRelaciones);
        return ResponseEntity.ok(productos);
    }

    // Buscar productos de una tienda ordenados por precio
    @GetMapping("/buscar-por-tienda-precio-desc")
    @Operation(summary = "Obtener productos de una tienda ordenados por precio descendente", description = "Obtiene los productos de la tienda, ordenados de mayor a menor segun el precio")
    public ResponseEntity<List<Producto>> buscarPorTiendaOrdenadoPorPrecio(
            @org.springframework.web.bind.annotation.RequestParam Integer idTienda) {
        List<Producto> productos = productoService.buscarPorTiendaOrdenadoPorPrecio(idTienda);
        if (productos.isEmpty())
            return ResponseEntity.noContent().build();
        productos.forEach(this::limpiarRelaciones);
        return ResponseEntity.ok(productos);
    }

    // Buscar productos con stock menor a cierto valor
    @GetMapping("/buscar-stock-bajo")
    @Operation(summary = "Obtener productos con bajo stock", description = "Obtiene una lista de productos cuyo stock es bajo")
    public ResponseEntity<List<Producto>> buscarConStockBajo(
            @org.springframework.web.bind.annotation.RequestParam Integer stock) {
        List<Producto> productos = productoService.buscarConStockBajo(stock);
        if (productos.isEmpty())
            return ResponseEntity.noContent().build();
        productos.forEach(this::limpiarRelaciones);
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    @Operation(summary = "Crear Producto", description = "Crea un nuevo producto")
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto) {
        Producto productoNuevo = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoNuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Producto por su ID", description = "Actualiza todos los datos de un producto ya existente")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        try {
            Producto productoYaExistente = productoService.findById(id);
            if (productoYaExistente == null) {
                return ResponseEntity.notFound().build();
            }

            productoYaExistente.setNombreProducto(producto.getNombreProducto());
            productoYaExistente.setDescripcionProducto(producto.getDescripcionProducto());
            productoYaExistente.setPrecioProducto(producto.getPrecioProducto());
            productoYaExistente.setStockProducto(producto.getStockProducto());
            productoYaExistente.setCategoria(producto.getCategoria());
            productoYaExistente.setTienda(producto.getTienda());

            Producto productoActualizado = productoService.save(productoYaExistente);
            return ResponseEntity.ok(productoActualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un Producto por ID", description = "Actualiza parcialmente los datos de un producto existente")
    public ResponseEntity<Producto> patchProducto(@PathVariable Integer id, @RequestBody Producto partialProducto) {
        try {
            Producto updatedProducto = productoService.patchProducto(id, partialProducto);

            if (updatedProducto.getCategoria() != null) {
                updatedProducto.getCategoria().setProductos(null);
            }
            if (updatedProducto.getTienda() != null) {
                updatedProducto.getTienda().setProductos(null);
            }

            return ResponseEntity.ok(updatedProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Producto por su ID", description = "Elimina un producto existente")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            productoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
