package com.valhallaride.valhallaride.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.valhallaride.valhallaride.assemblers.OrdenModelAssembler;
import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.service.OrdenService;

@RestController
@RequestMapping("/api/v2/ordenes")
public class OrdenControllerv2 {

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private OrdenModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Orden>> getAllOrdenes() { // Devuelve un "ColectionModel" que contiene una lista de órdenes con enlaces HATEOAS
        List<EntityModel<Orden>> ordenes = ordenService.findAll().stream() // Llama a service para obtener todas las órdenes, y usa stream() para recorrer cada una
            .map(assembler::toModel) // Convierte cada orden en un EntityModel
            .collect(Collectors.toList()); // Se recopila todos esos modelos en una lista

        return CollectionModel.of(ordenes, // Devuelve un CollectionModel que contiene la lista de órdenes
            linkTo(methodOn(OrdenControllerv2.class).getAllOrdenes()).withSelfRel()); // Incluye un enlace "self" que apunta a getAllOrdenes, permitiendo al cliente saber cómo volver a obtener esta colección
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Orden> getOrdenById(@PathVariable Integer id) { // Obtiene el ID de la orden, y devuelve EntityModel que es la orden con enlaces HATEOAS
        Orden orden = ordenService.findById(id); // Llama a service para buscar la orden por ID
        return assembler.toModel(orden); // Incluye enlaces como put, delete, etc.
        }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Orden>> crearOrden(@RequestBody Orden orden) {
        Orden nuevaOrden = ordenService.save(orden); // Guarda una orden usando service
        return ResponseEntity
            .created(linkTo(methodOn(OrdenControllerv2.class).getOrdenById(nuevaOrden.getIdOrden())).toUri()) // Usa HATEOAS para construir el enlace hacia la nueva orden creada y genera la URI apuntando a getOrdenById
            .body(assembler.toModel(nuevaOrden)); // Contiene todos los enlaces de HATEOAS (update, delete, etc)
        }
    
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Orden>> actualizarOrden(@PathVariable Integer id, @RequestBody Orden orden) {
        orden.setIdOrden(id); // Se asegura de que el objeto orden tenga el ID correcto antes de guardarlo
        Orden ordenActualizada = ordenService.save(orden); // Guarda la orden
        return ResponseEntity
                .ok(assembler.toModel(ordenActualizada)); // Devuelve 200 OK e incluye enlaces de HATEOAS(update, delete, etc)

    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<?> eliminarOrden(@PathVariable Integer id) { // Método que recibe una ID para eliminar una orden
            ordenService.delete(id); // Llama a service para eliminar la orden con el ID dado
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content 
        }
}
