package com.valhallaride.valhallaride.controller;

import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.service.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @GetMapping
    public ResponseEntity<List<Orden>> listarTodas() {
        List<Orden> ordenes = ordenService.findAll();
        if (ordenes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> buscarPorId(@PathVariable Integer id) {
        Orden orden = ordenService.findById(id);
        return (orden != null) ? ResponseEntity.ok(orden) : ResponseEntity.notFound().build();
    }

    @GetMapping("/por-usuario/{idUsuario}")
    public ResponseEntity<List<Orden>> buscarPorUsuario(@PathVariable Integer idUsuario) {
        List<Orden> ordenes = ordenService.findByUsuario(idUsuario);
        if (ordenes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/por-estado")
    public ResponseEntity<List<Orden>> buscarPorEstadoPago(@RequestParam Boolean pagado) {
        List<Orden> ordenes = ordenService.findByPagado(pagado);
        if (ordenes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ordenes);
    }

    @PostMapping
    public ResponseEntity<Orden> registrar(@RequestBody Orden orden) {
        Orden guardada = ordenService.save(orden);
        return ResponseEntity.ok(guardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orden> actualizar(@PathVariable Integer id, @RequestBody Orden ordenActualizada) {
        Orden orden = ordenService.update(id, ordenActualizada);
        return (orden != null) ? ResponseEntity.ok(orden) : ResponseEntity.notFound().build();
    }


    @PatchMapping("/{id}/pagado")
    public ResponseEntity<Orden> actualizarEstadoPago(@PathVariable Integer id, @RequestParam Boolean pagado) {
        Orden actualizada = ordenService.actualizarEstadoPago(id, pagado);
        return (actualizada != null) ? ResponseEntity.ok(actualizada) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        ordenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
