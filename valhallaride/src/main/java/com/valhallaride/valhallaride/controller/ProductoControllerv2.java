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

import com.valhallaride.valhallaride.assemblers.ProductoModelAssembler;
import com.valhallaride.valhallaride.model.Producto;
import com.valhallaride.valhallaride.service.ProductoService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/productos")
public class ProductoControllerv2 {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Producto>> getAllProductos(){
        List<EntityModel<Producto>> productos = productoService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoControllerv2.class).getAllProductos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Producto> getProductoById(@PathVariable Integer id){
        Producto producto = productoService.findById(id);
        return assembler.toModel(producto);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Producto>> crearProducto(@RequestBody Producto producto){
        Producto nuevoProducto = productoService.save(producto);
        return ResponseEntity
                .created(linkTo(methodOn(ProductoControllerv2.class).getProductoById(nuevoProducto.getIdProducto())).toUri())
                .body(assembler.toModel(nuevoProducto));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Integer id, @RequestBody Producto producto){
        producto.setIdProducto(id);
        Producto productoActualizado = productoService.save(producto);
        return ResponseEntity
                .ok(assembler.toModel(productoActualizado));   

        }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<?> eliminarProducto(@PathVariable Integer id){
            productoService.delete(id);
            return ResponseEntity.noContent().build();
        }
}
