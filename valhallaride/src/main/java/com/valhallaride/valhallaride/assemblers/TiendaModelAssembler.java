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

    @Override // Se sobrescribe el método toModel()
    public EntityModel<Tienda> toModel(Tienda tienda){ // Convierte un objeto "Tienda" en un EntityModel (enlaces HATEOAS)
        return EntityModel.of(tienda, // Se crea el EntityModel
        // Enlace a la tienda específica por ID
        linkTo(methodOn(TiendaControllerv2.class).getTiendaById(Long.valueOf(tienda.getIdTienda()))).withSelfRel(),
        // Enlace que permite obtener todas las tiendas
        linkTo(methodOn(TiendaControllerv2.class).getAllTiendas()).withRel("tiendas"),
        // Enlace que permite crear una tienda
        linkTo(methodOn(TiendaControllerv2.class).crearTienda(tienda)).withRel("crear_tienda"),
        // Enlace que permite actualizar una tienda
        linkTo(methodOn(TiendaControllerv2.class).actualizarTienda(Long.valueOf(tienda.getIdTienda()), tienda)).withRel("actualizar_tienda"),
        // Enlace que permite eliminar una tienda
        linkTo(methodOn(TiendaControllerv2.class).eliminarTienda(Long.valueOf(tienda.getIdTienda()))).withRel("eliminar_tienda"));
    }
}
