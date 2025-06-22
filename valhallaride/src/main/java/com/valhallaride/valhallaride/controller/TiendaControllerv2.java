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

@RestController
@RequestMapping("/api/v2/tiendas")
public class TiendaControllerv2 {

    @Autowired
    private TiendaService tiendaService;

    @Autowired
    private TiendaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Tienda>> getAllTiendas(){
        List<EntityModel<Tienda>> tiendas = tiendaService.findAll().stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());
    return CollectionModel.of(tiendas,
                linkTo(methodOn(TiendaControllerv2.class).getAllTiendas()).withSelfRel());        
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Tienda> getTiendaById(@PathVariable Long id){
            Tienda tienda = tiendaService.findById(id);
            return assembler.toModel(tienda);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Tienda>> crearTienda(@RequestBody Tienda tienda){
        Tienda nuevaTienda = tiendaService.save(tienda);
        return ResponseEntity
                .ok(assembler.toModel(nuevaTienda));
    
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Tienda>> actualizarTienda(@PathVariable Long id, @RequestBody Tienda tienda){
        tienda.setIdTienda(id);
        Tienda tiendaActualizada = tiendaService.save(tienda);
        return ResponseEntity
                .ok(assembler.toModel(tiendaActualizada));

        }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<?> eliminarTienda(@PathVariable Long id){
            tiendaService.delete(id);
            return ResponseEntity.noContent().build();

    }
}
