package com.valhallaride.valhallaride.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.valhallaride.valhallaride.controller.TiendaControllerv2;
import com.valhallaride.valhallaride.model.Tienda;

@Component
public class TiendaModelAssembler implements RepresentationModelAssembler<Tienda, EntityModel<Tienda>>{

    @Override
    public EntityModel<Tienda> toModel(Tienda tienda){
        return EntityModel.of(tienda,
        linkTo(methodOn(TiendaControllerv2.class).getTiendaById(Long.valueOf(tienda.getIdTienda()))).withSelfRel(),
        linkTo(methodOn(TiendaControllerv2.class).getAllTiendas()).withRel("tiendas"),
        linkTo(methodOn(TiendaControllerv2.class).crearTienda(tienda)).withRel("crear_tienda"),
        linkTo(methodOn(TiendaControllerv2.class).actualizarTienda(Long.valueOf(tienda.getIdTienda()), tienda)).withRel("actualizar_tienda"),
        linkTo(methodOn(TiendaControllerv2.class).eliminarTienda(Long.valueOf(tienda.getIdTienda()))).withRel("eliminar_tienda"));
    }
}
