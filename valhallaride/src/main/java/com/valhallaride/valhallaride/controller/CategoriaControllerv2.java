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

import io.swagger.v3.oas.annotations.Operation;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/categorias")
@Tag(name = "Categorias", description = "Operaciones relacionadas con las Categorias")
public class CategoriaControllerv2 {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)   // Devuelve datos en formato HALJSON(HATEOAS)
    @Operation(summary = "Obtener todas las categorias", description = "Obtiene una lista de todas las categorias")
    public CollectionModel<EntityModel<Categoria>> getAllCategorias(){
        List<EntityModel<Categoria>> categorias = categoriaService.findAll().stream()
                .map(assembler::toModel)    // Aqui usa el assembler para tener cada categoria con los enlaces HATEOAS
                .collect(Collectors.toList()); // Obtiene el resultado como lista
    
        return CollectionModel.of(categorias,
                linkTo(methodOn(CategoriaControllerv2.class).getAllCategorias()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener categorias por ID", description = "Obtiene categorias según su ID")
    public EntityModel<Categoria> getCategoriaById(@PathVariable Long id){
        Categoria categoria = categoriaService.findById(id); // Llama a service para obtener la categoria por un ID especifico
        return assembler.toModel(categoria); // Y aqui convierte la entidad categoria en un EntityModel con enlaces HATEOAS 
        }
    
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear Categorias", description = "Permite crear categorias")
    public ResponseEntity<EntityModel<Categoria>> crearCategoria(@RequestBody Categoria categoria){
        Categoria nuevCategoria = categoriaService.save(categoria);
        return ResponseEntity // .created indica que se creo un nuevo recurso
            .created(linkTo(methodOn(CategoriaControllerv2.class).getCategoriaById(Long.valueOf(nuevCategoria.getIdCategoria()))).toUri()) // Usa HATEOAS para construir el link de getCategoriaById, pasando el ID creado, y convierte ese enlace en una URI
            .body(assembler.toModel(nuevCategoria)); // Establece el cuerpo de la respuesta con el objeto nuevCategoria, en un EntityModel con enlaces de HATEOAS
        }
    
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar Categoria por su ID", description = "Actualiza los datos de una categoria ya existente")
    public ResponseEntity<EntityModel<Categoria>> actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria){
        categoria.setIdCategoria(id.intValue()); // Establece el ID con el objeto categoria recibido, conviertiendo el Long a int, asegurando que el objeto tenga el ID correcto antes de guardarlo
        Categoria categoriaActualizada = categoriaService.save(categoria); // Aqui llama al service para guardar (actualizar) la categoria con el nuevo contenido
        return ResponseEntity 
                .ok(assembler.toModel(categoriaActualizada)); // Devuelve una respuesta HTTP 200 OK con el cuerpo que incluye la categoria actualizada
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar parcialmente Categoria por su ID", description = "Permite actualizar parcialmente los datos de una categoria existente")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id){
        categoriaService.delete(id);    
        return ResponseEntity.noContent().build();
    }
}
