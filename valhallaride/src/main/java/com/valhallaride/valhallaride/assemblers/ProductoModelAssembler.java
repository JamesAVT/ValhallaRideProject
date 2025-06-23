package com.valhallaride.valhallaride.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.valhallaride.valhallaride.controller.ProductoControllerv2;
import com.valhallaride.valhallaride.model.Producto;

@Component // Marca esta clase como un componente de Spring
public class ProductoModelAssembler implements RepresentationModelAssembler <Producto, EntityModel<Producto>>{

    @Override // Método que sobrescrobe el toModel()
    public EntityModel<Producto> toModel(Producto producto) { // Recibe un objeto producto y devuelve un EntityModel, con enlaces HATEOAS
            return EntityModel.of(producto, // Se crea un EntityModel que envuelve al producto
            // Enlace al endpoit(URL que ofrece la API para realizar alguna operación) que devuelve este mismo producto por su ID
            linkTo(methodOn(ProductoControllerv2.class).getProductoById(producto.getIdProducto())).withSelfRel(),
            // Enlace que obtiene todos los productos
            linkTo(methodOn(ProductoControllerv2.class).getAllProductos()).withRel("productos"),
            // Enlace que permite crear un producto
            linkTo(methodOn(ProductoControllerv2.class).crearProducto(producto)).withRel("crear_producto"),
            // Enlace que permite actualizar un producto
            linkTo(methodOn(ProductoControllerv2.class).actualizarProducto(producto.getIdProducto(), producto)).withRel("actualizar_producto"),
            // Enlace que permite eliminar un producto
            linkTo(methodOn(ProductoControllerv2.class).eliminarProducto(producto.getIdProducto())).withRel("eliminar_producto"));
    }
}
