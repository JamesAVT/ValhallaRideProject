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




import java.util.List;

@RestController
@RequestMapping("/api/v1/producto-orden")
public class ProductoOrdenController {

    @Autowired
    private ProductoOrdenService productoOrdenService;

    @GetMapping
    public ResponseEntity<List<ProductoOrden>> listarTodos() {
        List<ProductoOrden> lista = productoOrdenService.findAll();
        if (lista.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoOrden> buscarPorId(@PathVariable Integer id) {
        ProductoOrden encontrado = productoOrdenService.findById(id);
        return (encontrado != null) ? ResponseEntity.ok(encontrado) : ResponseEntity.notFound().build();
    }

    @GetMapping("/por-orden/{idOrden}")
    public ResponseEntity<List<ProductoOrden>> buscarPorOrden(@PathVariable Integer idOrden) {
        List<ProductoOrden> lista = productoOrdenService.findByOrden(idOrden);
        if (lista.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/por-producto/{idProducto}")
    public ResponseEntity<List<ProductoOrden>> buscarPorProducto(@PathVariable Integer idProducto) {
        List<ProductoOrden> lista = productoOrdenService.findByProducto(idProducto);
        if (lista.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/ordenado-por-fecha")
    public ResponseEntity<List<ProductoOrden>> listarOrdenadoPorFecha() {
        List<ProductoOrden> lista = productoOrdenService.findAllOrderByFechaHoraDesc();
        if (lista.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<ProductoOrden> registrar(@RequestBody ProductoOrden productoOrden) {
        ProductoOrden guardado = productoOrdenService.save(productoOrden);
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoOrden> actualizar(@PathVariable Integer id, @RequestBody ProductoOrden productoOrden) {
        ProductoOrden existente = productoOrdenService.findById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        existente.setCantidad(productoOrden.getCantidad());
        existente.setFechaHora(productoOrden.getFechaHora());
        existente.setOrden(productoOrden.getOrden());
        existente.setPrecioProducto(productoOrden.getPrecioProducto());
        existente.setProducto(productoOrden.getProducto());

        ProductoOrden actualizado = productoOrdenService.save(existente);
        return ResponseEntity.ok(actualizado);
        
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductoOrden> patchProductoOrden(@PathVariable Integer id, @RequestBody ProductoOrden partialProductoOrden) {
        ProductoOrden existente = productoOrdenService.findById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
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

        ProductoOrden actualizado = productoOrdenService.save(existente);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        productoOrdenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
