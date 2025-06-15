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

@RestController
@RequestMapping("/api/v1/productos")
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
    // Para buscar necesitamos http://localhost:8080/api/v1/productos/buscar-por-nombre?nombre=leche

    @GetMapping("/buscar-por-nombre")
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
    public ResponseEntity<List<Producto>> buscarConStockBajo(
            @org.springframework.web.bind.annotation.RequestParam Integer stock) {
        List<Producto> productos = productoService.buscarConStockBajo(stock);
        if (productos.isEmpty())
            return ResponseEntity.noContent().build();
        productos.forEach(this::limpiarRelaciones);
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto) {
        Producto productoNuevo = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoNuevo);
    }

    @PutMapping("/{id}")
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
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            productoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
