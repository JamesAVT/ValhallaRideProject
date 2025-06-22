package com.valhallaride.valhallaride.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.valhallaride.valhallaride.controller.OrdenControllerv2;
import com.valhallaride.valhallaride.model.Orden;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class OrdenModelAssembler implements RepresentationModelAssembler<Orden, EntityModel<Orden>> {

    @Override
    public EntityModel<Orden> toModel(Orden orden) {
            return EntityModel.of(orden,
                    linkTo(methodOn(OrdenControllerv2.class).getOrdenById(orden.getIdOrden())).withSelfRel(),
                    linkTo(methodOn(OrdenControllerv2.class).getAllOrdenes()).withRel("ordenes"),
                    linkTo(methodOn(OrdenControllerv2.class).crearOrden(orden)).withRel("crear_ordenes"),
                    linkTo(methodOn(OrdenControllerv2.class).actualizarOrden(orden.getIdOrden(), orden)).withRel("actualizar_orden"),
                    linkTo(methodOn(OrdenControllerv2.class).eliminarOrden(orden.getIdOrden())).withRel("eliminar_orden"));
        }
}
