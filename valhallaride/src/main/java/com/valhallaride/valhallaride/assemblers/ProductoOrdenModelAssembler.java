package com.valhallaride.valhallaride.assemblers;

import org.springframework.stereotype.Component;

import com.valhallaride.valhallaride.controller.ProductoOrdenControllerv2;
import com.valhallaride.valhallaride.model.ProductoOrden;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

@Component
public class ProductoOrdenModelAssembler implements RepresentationModelAssembler<ProductoOrden, EntityModel<ProductoOrden>>{

    @Override
    public EntityModel<ProductoOrden> toModel(ProductoOrden productoOrden) {
        return EntityModel.of(productoOrden,
            linkTo(methodOn(ProductoOrdenControllerv2.class).getProdOrdenById(productoOrden.getIdProductoOrden())).withSelfRel(),
            linkTo(methodOn(ProductoOrdenControllerv2.class).getAllProdOrdenes()).withRel("produtoOrdenes"),
            linkTo(methodOn(ProductoOrdenControllerv2.class).crearProdOrden(productoOrden)).withRel("crar_producoOrdenes"),
            linkTo(methodOn(ProductoOrdenControllerv2.class).actualizarProdOrden(productoOrden.getIdProductoOrden(), productoOrden)).withRel("actualizar_productoOrden"),
            linkTo(methodOn(ProductoOrdenControllerv2.class).eliminarProdOrden(productoOrden.getIdProductoOrden())).withRel("eliminar_productoOrden"));
            }
}
