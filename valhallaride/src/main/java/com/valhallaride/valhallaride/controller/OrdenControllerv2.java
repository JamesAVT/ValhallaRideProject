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
    public CollectionModel<EntityModel<Orden>> getAllOrdenes() {
        List<EntityModel<Orden>> ordenes = ordenService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(ordenes,
            linkTo(methodOn(OrdenControllerv2.class).getAllOrdenes()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Orden> getOrdenById(@PathVariable Integer id) {
        Orden orden = ordenService.findById(id);
        return assembler.toModel(orden);
        }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Orden>> crearOrden(@RequestBody Orden orden) {
        Orden nuevaOrden = ordenService.save(orden);
        return ResponseEntity
            .created(linkTo(methodOn(OrdenControllerv2.class).getOrdenById(nuevaOrden.getIdOrden())).toUri())
            .body(assembler.toModel(nuevaOrden));
        }
    
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Orden>> actualizarOrden(@PathVariable Integer id, @RequestBody Orden orden) {
        orden.setIdOrden(id);
        Orden ordenActualizada = ordenService.save(orden);
        return ResponseEntity
                .ok(assembler.toModel(ordenActualizada));

    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<?> eliminarOrden(@PathVariable Integer id) {
            ordenService.delete(id);
            return ResponseEntity.noContent().build();
        }
}
