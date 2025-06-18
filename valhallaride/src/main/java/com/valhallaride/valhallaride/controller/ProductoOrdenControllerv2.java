package com.valhallaride.valhallaride.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.valhallaride.valhallaride.assemblers.ProductoOrdenModelAssembler;
import com.valhallaride.valhallaride.model.ProductoOrden;
import com.valhallaride.valhallaride.service.ProductoOrdenService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/productos_orden")
public class ProductoOrdenControllerv2 {

    @Autowired
    private ProductoOrdenService productoOrdenService;

    @Autowired
    private ProductoOrdenModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<ProductoOrden>> getAllProdOrdenes(){
        List<EntityModel<ProductoOrden>> prodOrdenes = productoOrdenService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(prodOrdenes, 
                linkTo(methodOn(ProductoOrdenControllerv2.class).getAllProdOrdenes()).withSelfRel());
    }

    @GetMapping(value = "/por-orden/{idOrden}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<ProductoOrden> getProdOrdenById(@PathVariable Integer Id){
        ProductoOrden productoOrden = productoOrdenService.findById(Id);
        return assembler.toModel(productoOrden);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoOrden>> crearProdOrden(@RequestBody ProductoOrden productoOrden){
        ProductoOrden nuevoProdOrden = productoOrdenService.save(productoOrden);
        return ResponseEntity
                .created(linkTo(methodOn(ProductoOrdenControllerv2.class).getProdOrdenById(nuevoProdOrden.getIdProductoOrden())).toUri())
                .body(assembler.toModel(nuevoProdOrden));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoOrden>> actualizarProdOrden(@PathVariable Integer id, @RequestBody ProductoOrden productoOrden){
        productoOrden.setIdProductoOrden(id);
        ProductoOrden prodOrdenActualizado = productoOrdenService.save(productoOrden);
        return ResponseEntity
                .ok(assembler.toModel(prodOrdenActualizado));

    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarProdOrden(@PathVariable Integer id){
        productoOrdenService.delete(id);
        return ResponseEntity.noContent().build();
        }

}
