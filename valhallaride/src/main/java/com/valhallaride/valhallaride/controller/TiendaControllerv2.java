package com.valhallaride.valhallaride.controller;



import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valhallaride.valhallaride.assemblers.TiendaModelAssembler;
import com.valhallaride.valhallaride.model.Tienda;
import com.valhallaride.valhallaride.service.TiendaService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/tiendas")
@Tag(name = "Tiendas", description = "Operaciones relacionadas con Tiendas")
public class TiendaControllerv2 {

    @Autowired
    private TiendaService tiendaService;

    @Autowired
    private TiendaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todas las tiendas", description = "Obtiene una lista de todas las tiendas")
    public CollectionModel<EntityModel<Tienda>> getAllTiendas(){ // Método para obtener todas las tiendas
        List<EntityModel<Tienda>> tiendas = tiendaService.findAll().stream() // Obtiene la lista de todas las tiendas llamando a service. stream transforma cada objeto Tienda en un EntityModel con enlaces
        .map(assembler::toModel)
        .collect(Collectors.toList());
    return CollectionModel.of(tiendas, // Devuelve un CollectionModel que contiene la lista de tiendas con sus enlaces 
                linkTo(methodOn(TiendaControllerv2.class).getAllTiendas()).withSelfRel()); // Y un enlace self(a la URL para obtener todas las tiendas)       
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener una tienda por ID", description = "Obtiene una tienda especificada por su ID")
    public EntityModel<Tienda> getTiendaById(@PathVariable Long id){ // Método para obtener una tienda específica por su ID
            Tienda tienda = tiendaService.findById(id); // Llama a service para buscar la tienda por su ID
            return assembler.toModel(tienda); // Devuelve el objeto Tienda en un EntityModel, que tiene enlaces HATEOAS (actualizar, eliminar, etc)
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear Tienda", description = "Crea una nueva tienda")
    public ResponseEntity<EntityModel<Tienda>> crearTienda(@RequestBody Tienda tienda){ // Método para crear una nueva tienda
        Tienda nuevaTienda = tiendaService.save(tienda); // Llama a service para guardar la nueva tienda
        return ResponseEntity // Devuelve 200 OK
                .ok(assembler.toModel(nuevaTienda)); // La respuesta contiene la tienda creada, en un EntityModel que agrega enlaces HATEOAS
    
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar Tienda por su ID", description = "Actualiza todos los datos de una tienda existente")
    public ResponseEntity<EntityModel<Tienda>> actualizarTienda(@PathVariable Long id, @RequestBody Tienda tienda){ // Método para actualizar una tienda por su ID
        tienda.setIdTienda(id); // Asigna el ID recibido al objeto tienda para asegurar que se actualice la tienda correcta
        Tienda tiendaActualizada = tiendaService.save(tienda); // Llama a service para guardar (actualizar) la tienda con los datos dados
        return ResponseEntity // Devuelve una respuesta 200 OK
                .ok(assembler.toModel(tiendaActualizada)); // Contiene la tienda actualizada, en un EntityModel con los enlaces HATEOAS

        }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar Tienda por ID", description = "Elimina una tienda existente")
        public ResponseEntity<?> eliminarTienda(@PathVariable Long id){ // Método para eliminar una tienda por su ID
            tiendaService.delete(id); // Llama a service para eliminar la tienda con el ID especificado
            return ResponseEntity.noContent().build(); // Devuelve respuesta 204 No Content

    }
}
