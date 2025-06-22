package com.valhallaride.valhallaride.controller;

import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.service.OrdenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@Tag(name = "Ordenes", description = "Operaciones relacionadas con Ordenes")

public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @GetMapping
    @Operation(summary = "Obtener todas las ordenes", description = "Obtiene una lista de todas las ordenes ")
    public ResponseEntity<List<Orden>> listarTodas() { // Metodo para obtener todas las ordenes registradas
        List<Orden> ordenes = ordenService.findAll();
        if (ordenes.isEmpty()) return ResponseEntity.noContent().build(); // Si la lista esta vacia, responde con el codigo HTTP 204 No Content
        return ResponseEntity.ok(ordenes); // Si hay ordenes, responde con codigo 200 OK y el cuerpo con la lista de ordenes
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener Ordenes por su ID", description = "Obtiene una orden existente")
    public ResponseEntity<Orden> buscarPorId(@PathVariable Integer id) { // Metodo para buscar una orden por su ID 
        Orden orden = ordenService.findById(id); // Llama a service para buscar la orden con ese ID
        return (orden != null) ? ResponseEntity.ok(orden) : ResponseEntity.notFound().build(); // Si la orden existe, HTTP 200 Ok | Si no existe, HTTP 404 Not Found
    }

    @GetMapping("/por-usuario/{idUsuario}")
    @Operation(summary = "Obtener ordenes por ID de usuario", description = "Obtiene todas las ordenes realizadas por un usuario especifico, por su ID")
    public ResponseEntity<List<Orden>> buscarPorUsuario(@PathVariable Integer idUsuario) { // Metodo para obtener las órdenes de un usuario específico
        List<Orden> ordenes = ordenService.findByUsuario(idUsuario); // Llama a service para buscar todas las ordenes asociadas al usuario con ese ID
        if (ordenes.isEmpty()) return ResponseEntity.noContent().build(); // Si la lista esta vacia, responde con un codigo 204 No Content      
        return ResponseEntity.ok(ordenes); // Si hay ordenes, responde con codigo 200 OK y la lista como cuerpo
    }

    @GetMapping("/por-estado")
    @Operation(summary = "Obtener ordenes por estado", description = "Obtiene todas las ordenes que coinciden con un estado especifico (pendiente, completada, cancelada)")
    public ResponseEntity<List<Orden>> buscarPorEstadoPago(@RequestParam Boolean pagado) { // Metodo para buscar ordenes segun el estado de pago
        List<Orden> ordenes = ordenService.findByPagado(pagado); // Llama al service para obtener las órdenes que coinciden con el valor de "pagado"
        if (ordenes.isEmpty()) return ResponseEntity.noContent().build(); // Si no se encuentran órdenes con este estado de pago, responde con 204 No Content
        return ResponseEntity.ok(ordenes); // Y si hay órdenes, responde con 200 OK
    }

    @PostMapping
    @Operation(summary = "Crear Orden", description = "Crea una orden")
    public ResponseEntity<Orden> registrar(@RequestBody Orden orden) {
        Orden guardada = ordenService.save(orden);
        return ResponseEntity.ok(guardada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Orden por su ID", description = "Actualiza todos los datos de una orden existente")
    public ResponseEntity<Orden> actualizar(@PathVariable Integer id, @RequestBody Orden ordenActualizada) { // Método para actualizar una orden existente
        Orden orden = ordenService.update(id, ordenActualizada); // Llama a service para actualizar la orden con el ID dado
        return (orden != null) ? ResponseEntity.ok(orden) : ResponseEntity.notFound().build(); // Si la orden fue encontrada y actualizada, responde con 200 OK y la orden modificada
    }                                                                                          // Si no se econtró, responde con un 404 Not Found


    @PatchMapping("/{id}/pagado")
    @Operation(summary = "Marcar una orden como pagada", description = "Actualiza el estado de una orden especificada por su ID, marcandola como pagada")
    public ResponseEntity<Orden> actualizarEstadoPago(@PathVariable Integer id, @RequestParam Boolean pagado) { //Método para cambiar solo el estado de pago de una orden
        Orden actualizada = ordenService.actualizarEstadoPago(id, pagado); // Llama a service para actualizar el campo pagado de la orden con ese ID
        return (actualizada != null) ? ResponseEntity.ok(actualizada) : ResponseEntity.notFound().build(); // Si se pudo actualizar la orden, devuelve 200 OK con la orden actualizada
    }                                                                                                     // Si no se encontró la orden, devuelve 404 Not Found

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Orden por su ID", description = "Elimina una orden existente")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) { // Método para eliminar una orden según su ID
        ordenService.delete(id); // Llama a service para eliminar la orden con una ID especificada
        return ResponseEntity.noContent().build(); // Devuelve un código HTTP 204 No Content, indicando así que la eliminación fue exitosa
    }
}
