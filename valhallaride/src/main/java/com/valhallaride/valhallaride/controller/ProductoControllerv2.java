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
    public CollectionModel<EntityModel<Producto>> getAllProductos(){ // Devuelve una colección de productos, donde cada producto tiene enlaces HATEOAS
        List<EntityModel<Producto>> productos = productoService.findAll().stream() // Llama a service para obtener todos los productos, y se usa stream() para convertir cada producto en un EntityModel
            .map(assembler::toModel) // Agrega los enlaces HATEOAS (actualizar, eliminar, etc)
            .collect(Collectors.toList());
        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoControllerv2.class).getAllProductos()).withSelfRel()); // Crea una colección HATEOAS con la lista de productos y un enlace "withSelfRel()", que permite que el cliente sepa de dónde vino esa colección
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Producto> getProductoById(@PathVariable Integer id){
        Producto producto = productoService.findById(id); // Llama a Service para buscar el producto usando el ID
        return assembler.toModel(producto); // Devuelve el producto en un EntityModel (assembler), agregando enlaces HATEOAS (actualizar, eliminar, etc)
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Producto>> crearProducto(@RequestBody Producto producto){
        Producto nuevoProducto = productoService.save(producto); // Guarda el nuevo producto 
        return ResponseEntity 
                .created(linkTo(methodOn(ProductoControllerv2.class).getProductoById(nuevoProducto.getIdProducto())).toUri())
                .body(assembler.toModel(nuevoProducto)); // Usa HATEOAS para construir un enlace al producto recién creado (getProductoById)
    }                                                   // toUri() convierte ese enlace en una URL. created genera una respuesta 201 created y devuelve el producto


    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Integer id, @RequestBody Producto producto){
        producto.setIdProducto(id); // Asegura que el objeto producto tenga el ID correcto que viene por la URL (para evitar que se cree uno nuevo por error)
        Producto productoActualizado = productoService.save(producto); // Guarda el prdocuto 
        return ResponseEntity
                .ok(assembler.toModel(productoActualizado)); // Devuelve 200 OK con el producto actualizado, convertido en EntityModel para incluir enlaces HATEOAS

        }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<?> eliminarProducto(@PathVariable Integer id){
            productoService.delete(id); // Llama a service para eliminar el producto identificado por el ID
            return ResponseEntity.noContent().build(); // Devuelve respuesta 204 No Content, indicando que se eliminó correctamente
        }
}
