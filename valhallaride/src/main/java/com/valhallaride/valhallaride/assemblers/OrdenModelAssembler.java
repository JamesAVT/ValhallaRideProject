package com.valhallaride.valhallaride.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.valhallaride.valhallaride.controller.OrdenControllerv2;
import com.valhallaride.valhallaride.model.Orden;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component // Marca esta clase como un componente de Spring
public class OrdenModelAssembler implements RepresentationModelAssembler<Orden, EntityModel<Orden>> {
    
    @Override // Sobrescribe el método "toModel()"
    public EntityModel<Orden> toModel(Orden orden) { // Recibe una instancia de Orden y devuelve un EntityModel, que contiene enlaces HATEOAS
            return EntityModel.of(orden, // Se crea un EntityModel para el objeto orden, y se le agregan enlaces relacionados
                    // Enlace self, apunta al endpoint(URL que ofrece la API para realizar alguna operación) que devuelve esta orden según su ID            
                    linkTo(methodOn(OrdenControllerv2.class).getOrdenById(orden.getIdOrden())).withSelfRel(),
                    // Enlace que permite obtener todas las órdenes
                    linkTo(methodOn(OrdenControllerv2.class).getAllOrdenes()).withRel("ordenes"),
                    // Enlace que permite creae una nueva orden
                    linkTo(methodOn(OrdenControllerv2.class).crearOrden(orden)).withRel("crear_ordenes"),
                    // Enlace que permite actualizar la orden
                    linkTo(methodOn(OrdenControllerv2.class).actualizarOrden(orden.getIdOrden(), orden)).withRel("actualizar_orden"),
                    // Enlace que permite eliminar la orden
                    linkTo(methodOn(OrdenControllerv2.class).eliminarOrden(orden.getIdOrden())).withRel("eliminar_orden"));
        }
}
