package com.valhallaride.valhallaride.controller;

import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.service.OrdenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@Tag(name = "Ordenes", description = "Operaciones relacionadas con Ordenes")

public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @GetMapping
    @Operation(summary = "Obtener todas las ordenes", description = "Obtiene una lista de todas las ordenes ")
    public ResponseEntity<List<Orden>> listarTodas() {
        List<Orden> ordenes = ordenService.findAll();
        if (ordenes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener Ordenes por su ID", description = "Obtiene una orden existente")
    public ResponseEntity<Orden> buscarPorId(@PathVariable Integer id) {
        Orden orden = ordenService.findById(id);
        return (orden != null) ? ResponseEntity.ok(orden) : ResponseEntity.notFound().build();
    }

    @GetMapping("/por-usuario/{idUsuario}")
    @Operation(summary = "Obtener ordenes por ID de usuario", description = "Obtiene todas las ordenes realizadas por un usuario especifico, por su ID")
    public ResponseEntity<List<Orden>> buscarPorUsuario(@PathVariable Integer idUsuario) {
        List<Orden> ordenes = ordenService.findByUsuario(idUsuario);
        if (ordenes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/por-estado")
    @Operation(summary = "Obtener ordenes por estado", description = "Obtiene todas las ordenes que coinciden con un estado especifico (pendiente, completada, cancelada)")
    public ResponseEntity<List<Orden>> buscarPorEstadoPago(@RequestParam Boolean pagado) {
        List<Orden> ordenes = ordenService.findByPagado(pagado);
        if (ordenes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ordenes);
    }

    @PostMapping
    @Operation(summary = "Crear Orden", description = "Crea una orden")
    public ResponseEntity<Orden> registrar(@RequestBody Orden orden) {
        Orden guardada = ordenService.save(orden);
        return ResponseEntity.ok(guardada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Orden por su ID", description = "Actualiza todos los datos de una orden existente")
    public ResponseEntity<Orden> actualizar(@PathVariable Integer id, @RequestBody Orden ordenActualizada) {
        Orden orden = ordenService.update(id, ordenActualizada);
        return (orden != null) ? ResponseEntity.ok(orden) : ResponseEntity.notFound().build();
    }


    @PatchMapping("/{id}/pagado")
    @Operation(summary = "Marcar una orden como pagada", description = "Actualiza el estado de una orden especificada por su ID, marcandola como pagada")
    public ResponseEntity<Orden> actualizarEstadoPago(@PathVariable Integer id, @RequestParam Boolean pagado) {
        Orden actualizada = ordenService.actualizarEstadoPago(id, pagado);
        return (actualizada != null) ? ResponseEntity.ok(actualizada) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Orden por su ID", description = "Elimina una orden existente")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        ordenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
