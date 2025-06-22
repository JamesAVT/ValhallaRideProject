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
    public ResponseEntity<List<Tienda>> listar() { // Método para listar todas las tiendas
        List<Tienda> tiendas = tiendaService.findAll(); // Llama al service para obtener todas las tiendas registradas
        if (tiendas.isEmpty()) { // Verifica si la lista de tiendas está vacía
            return ResponseEntity.noContent().build(); // Si no hay tiendas, responde con 204 No Content
        }
        return ResponseEntity.ok(tiendas); // Y si hay tiendas, respondee con 200 OK
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una tienda por ID", description = "Obtiene una tienda especificada por su ID")
    public ResponseEntity<Tienda> buscar(@PathVariable Long id) { // Método para buscar una tienda por su ID
        Tienda tienda = tiendaService.findById(id); // Llama a service para buscar una tienda con ese ID 
        if (tienda != null) { // Si no encuentra la tienda
            return ResponseEntity.ok(tienda); // Responde con 200 OK
        } else { // Y si no la encuentra
            return ResponseEntity.notFound().build(); // Responde con 404 Not Found
        }
            
    }

    @PostMapping
    @Operation(summary = "Crear Tienda", description = "Crea una nueva tienda")
    public ResponseEntity<Tienda> guardar(@RequestBody Tienda tienda) { // Método para crear una tienda
        Tienda tiendaNueva = tiendaService.save(tienda); // Llama a service para guardar la tienda nueva
        return ResponseEntity.status(HttpStatus.CREATED).body(tiendaNueva); // Y devuelve una respuesta 201 Created
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Tienda por su ID", description = "Actualiza todos los datos de una tienda existente")
    public ResponseEntity<Tienda> actualizar(@PathVariable Long id, @RequestBody Tienda tienda) { // Método para actualizar la tienda por su ID
        Tienda tiendaActualizada = tiendaService.updateTienda(id, tienda); // Llama a service para actualizar la tienda con el ID especificado

        if (tiendaActualizada != null){ // Si la tienda fue actualizada
            return ResponseEntity.ok(tiendaActualizada); // Responde con 200 OK
        } else { // y si no se encontró la tienda 
            return ResponseEntity.notFound().build(); // Responde con 404 Not Found
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una Tienda por ID", description = "Actualiza parcialmente los datos de una tienda existente")
    public ResponseEntity<Tienda> patchTienda(@PathVariable Long id, @RequestBody Tienda partialTienda) { // Método para actulizar una tienda por su ID
        try { // Intenta actualizar parcialmente la tienda usando service
            Tienda updatedTienda = tiendaService.patchTienda(id, partialTienda);
            return ResponseEntity.ok(updatedTienda); // Si la actualización es exitosa, devuelve 200 OK
        } catch (RuntimeException e) { // Y en caso de que no se actualice 
            return ResponseEntity.notFound().build(); // Responde con 404 Not Found
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Tienda por ID", description = "Elimina una tienda existente")
    public ResponseEntity<?> eliminar(@PathVariable Long id) { // Método para eliminar una tienda por su ID
        try { // Intenta eliminar la tienda con el ID dado llamando al service
            tiendaService.delete(id);
            return ResponseEntity.noContent().build(); // Si la eliminación es exitosa, responde con 204 No Content
        } catch (Exception e) { // Y si no se elimina 
            return ResponseEntity.notFound().build(); // Responde con 404 Not Found
        }
    }
}
