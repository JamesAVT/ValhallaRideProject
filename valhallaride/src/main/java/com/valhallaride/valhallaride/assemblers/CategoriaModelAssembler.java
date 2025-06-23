package com.valhallaride.valhallaride.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.valhallaride.valhallaride.controller.CategoriaControllerv2;
import com.valhallaride.valhallaride.model.Categoria;


@Component // Marca esta clase como un componente de Spring
public class CategoriaModelAssembler implements RepresentationModelAssembler<Categoria, EntityModel<Categoria>> {

    @Override // Este método sobrescribe toModel()
    public EntityModel<Categoria> toModel(Categoria categoria) { // Convierte categoría en un EntityModel, que es un objeto con enlaces (HATEOAS)
        return EntityModel.of(categoria, // Se crea el EntityModel
                // Agrega un enlace self, apuntando al método que obtiene esta misma categoría por su ID
                linkTo(methodOn(CategoriaControllerv2.class).getCategoriaById(Long.valueOf(categoria.getIdCategoria()))).withSelfRel(),
                // Agrega un link con la relación de categorías, que apunta para listar todas las categorías
                linkTo(methodOn(CategoriaControllerv2.class).getAllCategorias()).withRel("categorias"),
                // Agrega un link para crear una categoría
                linkTo(methodOn(CategoriaControllerv2.class).crearCategoria(categoria)).withRel("crar_categoria"),
                // Enlace para actualizar la categoría
                linkTo(methodOn(CategoriaControllerv2.class).actualizarCategoria(Long.valueOf(categoria.getIdCategoria()), categoria)).withRel("actualizar_categoria"),
                // Enlace para eliminar categoría
                linkTo(methodOn(CategoriaControllerv2.class).eliminarCategoria(Long.valueOf(categoria.getIdCategoria()))).withRel("eliminar_categoria"));
    }
}
