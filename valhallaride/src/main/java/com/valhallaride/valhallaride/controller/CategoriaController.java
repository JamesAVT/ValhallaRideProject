package com.valhallaride.valhallaride.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valhallaride.valhallaride.model.Categoria;
import com.valhallaride.valhallaride.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorias", description = "Operaciones relacionadas con las Categorias")

public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

   @GetMapping
   @Operation(summary = "Obtener todas las categorias", description = "Obtiene una lista de todas las categorias")
    public ResponseEntity<List<Categoria>> listar() {
        List<Categoria> categorias = categoriaService.findAll();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        for (Categoria c : categorias) {    // Recorre cada categoria en la lista
            if (c.getProductos() != null) { 
                c.getProductos().forEach(p -> { // Por cada producto de esa categoria
                    p.setCategoria(null); // elimina la referencia a la categoria del producto para evitar bucles infinitos (JSON)
                    if (p.getTienda() != null) {    // Si el producto tiene una tienda asociada
                        p.getTienda().setProductos(null); // elimina la lista de productos de la tienda 
                    }
                });
            }
        }

        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categorias por ID", description = "Obtiene categorias según su ID")
    public ResponseEntity<Categoria> buscar(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.findById(id);
            if (categoria == null) {    // Si no encuentra la categoria
                return ResponseEntity.notFound().build(); // Devuelve not found
            }


            if (categoria.getProductos() != null) {    
                categoria.getProductos().forEach(p -> {     // Recorre los productos de la categoria y quita la referencia de categoria del producto
                    p.setCategoria(null);         
                    if (p.getTienda() != null) {           // Si el producto tiene una tienda asociada
                        p.getTienda().setProductos(null);   // Borra la lista de productos de la tienda para evitar bucles
                    }
                });
            }

            return ResponseEntity.ok(categoria);
        } catch (Exception e) { // Maneja excepciones y evita que el programa se caiga
            e.printStackTrace(); // Imprime el error en la consola 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear Categorias", description = "Permite crear categorias")
    public ResponseEntity<Categoria> guardar(@RequestBody Categoria categoria) {
        Categoria categoriaNueva = categoriaService.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaNueva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Categoria por su ID", description = "Actualiza los datos de una categoria ya existente")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @RequestBody Categoria categoria) {
        Categoria catActualizada = categoriaService.updateCategoria(id, categoria);
        if (catActualizada != null){    // Si la categoria existe
            if (catActualizada.getProductos() != null){  // Si la categoria actualizada tiene productos asociados
                catActualizada.getProductos().forEach(p -> { // Recorre los productos para limpiar referencias 
                    p.setCategoria(null);   // Evita la referencia desde producto a la categoria (para no tener bucles)
                    if (p.getTienda() != null) {    // Si el producto tiene una tienda asociada
                        p.getTienda().setProductos(null); // Borra la lista de productos de la tienda
                    }
                });
            }
            return ResponseEntity.ok(catActualizada); // Devuelve la categoria actualizada
        } else {
            return ResponseEntity.notFound().build(); // Y si no encontró la categoria, responde con un not found
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente Categoria por su ID", description = "Permite actualizar parcialmente los datos de una categoria existente")
    public ResponseEntity<Categoria> patchCategoria(@PathVariable Long id, @RequestBody Categoria partialCategoria) {
        try {
            // Llama a service para hacer una actualización parcial
            Categoria updatedCategoria = categoriaService.patchCategoria(id, partialCategoria);

            if (updatedCategoria.getProductos() != null){ // Si la categoria tiene productos asociados
                updatedCategoria.getProductos().forEach(p -> {
                    p.setCategoria(null);   // Elimina la referencia de categoria para evitar bucles
                    if (p.getTienda() != null){     // Si el producto tiene una tienda asociada
                        p.getTienda().setProductos(null);  // Elimina la lista de productos de la tienda
                    }
                });
            }
            return ResponseEntity.ok(updatedCategoria); // Retorna la categoria parcialmente actualizada 
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Categoria por su ID", description = "Elimina una categoria existente")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            categoriaService.delete(id);    // Intenta eliminar la categoria con un ID en especifico
            return ResponseEntity.noContent().build(); // Si se elimina, devuelve un 204 No Content
        } catch (Exception e) {     // Y si es que no encontró el ID
            return ResponseEntity.notFound().build(); // responde con un 404 Not Found
        }
    }
}
