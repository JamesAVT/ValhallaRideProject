package com.valhallaride.valhallaride.assemblers;

import org.springframework.stereotype.Component;

import com.valhallaride.valhallaride.controller.ProductoOrdenControllerv2;
import com.valhallaride.valhallaride.model.ProductoOrden;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

@Component
public class ProductoOrdenModelAssembler implements RepresentationModelAssembler<ProductoOrden, EntityModel<ProductoOrden>>{

    @Override // Sobrescribe el método toModel()
    public EntityModel<ProductoOrden> toModel(ProductoOrden productoOrden) { // Recibe un objeto ProductoOrden y lo convierte en un EntityModel (Enlaces HATEOAS)
        return EntityModel.of(productoOrden, // Se crea el EntityModel
            // Enlace que permite consultar esa misma orden de producto por su ID
            linkTo(methodOn(ProductoOrdenControllerv2.class).getProdOrdenById(productoOrden.getIdProductoOrden())).withSelfRel(),
            // Enlace que obtiene todos los producto orden
            linkTo(methodOn(ProductoOrdenControllerv2.class).getAllProdOrdenes()).withRel("produtoOrdenes"),
            // Enlace que permite crear un producto orden
            linkTo(methodOn(ProductoOrdenControllerv2.class).crearProdOrden(productoOrden)).withRel("crar_producoOrdenes"),
            // Enlace que permite actualizar un producto orden
            linkTo(methodOn(ProductoOrdenControllerv2.class).actualizarProdOrden(productoOrden.getIdProductoOrden(), productoOrden)).withRel("actualizar_productoOrden"),
            // Enlace que permite eliminar un producto orden 
            linkTo(methodOn(ProductoOrdenControllerv2.class).eliminarProdOrden(productoOrden.getIdProductoOrden())).withRel("eliminar_productoOrden"));
            }
}
