package com.valhallaride.valhallaride.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.valhallaride.valhallaride.controller.CategoriaControllerv2;
import com.valhallaride.valhallaride.model.Categoria;


@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<Categoria, EntityModel<Categoria>> {

    @Override
    public EntityModel<Categoria> toModel(Categoria categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaControllerv2.class).getCategoriaById(Long.valueOf(categoria.getIdCategoria()))).withSelfRel(),
                linkTo(methodOn(CategoriaControllerv2.class).getAllCategorias()).withRel("categorias"),
                linkTo(methodOn(CategoriaControllerv2.class).crearCategoria(categoria)).withRel("crar_categoria"),
                linkTo(methodOn(CategoriaControllerv2.class).actualizarCategoria(Long.valueOf(categoria.getIdCategoria()), categoria)).withRel("actualizar_categoria"),
                linkTo(methodOn(CategoriaControllerv2.class).eliminarCategoria(Long.valueOf(categoria.getIdCategoria()))).withRel("eliminar_categoria"));
    }
}
