package com.valhallaride.valhallaride.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.valhallaride.valhallaride.controller.ProductoControllerv2;
import com.valhallaride.valhallaride.model.Producto;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler <Producto, EntityModel<Producto>>{

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
            return EntityModel.of(producto,
            linkTo(methodOn(ProductoControllerv2.class).getProductoById(producto.getIdProducto())).withSelfRel(),
            linkTo(methodOn(ProductoControllerv2.class).getAllProductos()).withRel("productos"),
            linkTo(methodOn(ProductoControllerv2.class).crearProducto(producto)).withRel("crear_producto"),
            linkTo(methodOn(ProductoControllerv2.class).actualizarProducto(producto.getIdProducto(), producto)).withRel("actualizar_producto"),
            linkTo(methodOn(ProductoControllerv2.class).eliminarProducto(producto.getIdProducto())).withRel("eliminar_producto"));
    }
}
