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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        List<EntityModel<ProductoOrden>> prodOrdenes = productoOrdenService.findAll().stream() // Llama a service para obtener todos los ProductoOrden y con stream transforma cada ProductoOrden en un EntityModel
            .map(assembler::toModel)  
            .collect(Collectors.toList()); // Se añaden enlaces (actualizar, delete, etc)
        return CollectionModel.of(prodOrdenes, // Devuekve una colección con la lista de EntityModel
                linkTo(methodOn(ProductoOrdenControllerv2.class).getAllProdOrdenes()).withSelfRel()); 
    }

    @GetMapping(value = "/por-orden/{idOrden}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<ProductoOrden> getProdOrdenById(@PathVariable Integer Id){ // Metodo para obtener ProductoOrden por ID
        ProductoOrden productoOrden = productoOrdenService.findById(Id); // Busca el ProductoOrden en el service por el ID recibido
        return assembler.toModel(productoOrden); // Devuelve el objeto encontrado
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoOrden>> crearProdOrden(@RequestBody ProductoOrden productoOrden){ // Método para crear un nuevo ProductoOrden
        ProductoOrden nuevoProdOrden = productoOrdenService.save(productoOrden); // Guarda el nuevo ProductoOrden en el service
        return ResponseEntity // Devuelve 201 Created | Y el cuerpo tiene el objeto ProductoOrden con los enlaces HATEOAS mediante el assembler
                .created(linkTo(methodOn(ProductoOrdenControllerv2.class).getProdOrdenById(nuevoProdOrden.getIdProductoOrden())).toUri())
                .body(assembler.toModel(nuevoProdOrden));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoOrden>> actualizarProdOrden(@PathVariable Integer id, @RequestBody ProductoOrden productoOrden){
        productoOrden.setIdProductoOrden(id); // Asigna el ID recibido en el objeto ProductoOrden
        ProductoOrden prodOrdenActualizado = productoOrdenService.save(productoOrden); // Llama a service para guardar (actualizar) el objeto
        return ResponseEntity
                .ok(assembler.toModel(prodOrdenActualizado)); // Devuelve un 200 OK y el objeto actualizado con EntityModel con enlaces HATEOAS

    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarProdOrden(@PathVariable Integer id){ // Método para eliminar un ProductoOrden por su ID
        productoOrdenService.delete(id); // Llama a service para eliminar el ProductoOrden con el ID
        return ResponseEntity.noContent().build(); // Devuelve una respuesta 204 Not Content, indicando que la eliminación funcionó correctamente
        }

}
