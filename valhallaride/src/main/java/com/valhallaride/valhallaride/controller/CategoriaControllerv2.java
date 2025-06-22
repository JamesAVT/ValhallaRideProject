package com.valhallaride.valhallaride.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valhallaride.valhallaride.assemblers.CategoriaModelAssembler;
import com.valhallaride.valhallaride.model.Categoria;
import com.valhallaride.valhallaride.service.CategoriaService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/categorias")
public class CategoriaControllerv2 {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Categoria>> getAllCategorias(){
        List<EntityModel<Categoria>> categorias = categoriaService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    
        return CollectionModel.of(categorias,
                linkTo(methodOn(CategoriaControllerv2.class).getAllCategorias()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Categoria> getCategoriaById(@PathVariable Long id){
        Categoria categoria = categoriaService.findById(id);
        return assembler.toModel(categoria);
        }
    
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Categoria>> crearCategoria(@RequestBody Categoria categoria){
        Categoria nuevCategoria = categoriaService.save(categoria);
        return ResponseEntity
            .created(linkTo(methodOn(CategoriaControllerv2.class).getCategoriaById(Long.valueOf(nuevCategoria.getIdCategoria()))).toUri())
            .body(assembler.toModel(nuevCategoria));
        }
    
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Categoria>> actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria){
        categoria.setIdCategoria(id.intValue());
        Categoria categoriaActualizada = categoriaService.save(categoria);
        return ResponseEntity
                .ok(assembler.toModel(categoriaActualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id){
        categoriaService.delete(id);    
        return ResponseEntity.noContent().build();
    }
}
