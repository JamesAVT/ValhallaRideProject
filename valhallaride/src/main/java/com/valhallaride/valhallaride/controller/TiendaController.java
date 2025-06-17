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

import com.valhallaride.valhallaride.model.Tienda;
import com.valhallaride.valhallaride.service.TiendaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/tiendas")
@Tag(name = "Tiendas", description = "Operaciones relacionadas con Tiendas")

public class TiendaController {

    @Autowired
    private TiendaService tiendaService;

    @GetMapping
    @Operation(summary = "Obtener todas las tiendas", description = "Obtiene una lista de todas las tiendas")
    public ResponseEntity<List<Tienda>> listar() {
        List<Tienda> tiendas = tiendaService.findAll();
        if (tiendas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tiendas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una tienda por ID", description = "Obtiene una tienda especificada por su ID")
    public ResponseEntity<Tienda> buscar(@PathVariable Long id) {
        Tienda tienda = tiendaService.findById(id);
        if (tienda != null) {
            return ResponseEntity.ok(tienda);
        } else {
            return ResponseEntity.notFound().build();
        }
            
    }

    @PostMapping
    @Operation(summary = "Crear Tienda", description = "Crea una nueva tienda")
    public ResponseEntity<Tienda> guardar(@RequestBody Tienda tienda) {
        Tienda tiendaNueva = tiendaService.save(tienda);
        return ResponseEntity.status(HttpStatus.CREATED).body(tiendaNueva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Tienda por su ID", description = "Actualiza todos los datos de una tienda existente")
    public ResponseEntity<Tienda> actualizar(@PathVariable Long id, @RequestBody Tienda tienda) {
        Tienda tiendaActualizada = tiendaService.updateTienda(id, tienda);

        if (tiendaActualizada != null){
            return ResponseEntity.ok(tiendaActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una Tienda por ID", description = "Actualiza parcialmente los datos de una tienda existente")
    public ResponseEntity<Tienda> patchTienda(@PathVariable Long id, @RequestBody Tienda partialTienda) {
        try {
            Tienda updatedTienda = tiendaService.patchTienda(id, partialTienda);
            return ResponseEntity.ok(updatedTienda);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Tienda por ID", description = "Elimina una tienda existente")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            tiendaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
