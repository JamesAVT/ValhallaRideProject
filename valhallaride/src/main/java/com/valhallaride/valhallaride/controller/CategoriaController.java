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

        for (Categoria c : categorias) {
            if (c.getProductos() != null) {
                c.getProductos().forEach(p -> {
                    p.setCategoria(null);
                    if (p.getTienda() != null) {
                        p.getTienda().setProductos(null);
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
            if (categoria == null) {
                return ResponseEntity.notFound().build();
            }


            if (categoria.getProductos() != null) {
                categoria.getProductos().forEach(p -> {
                    p.setCategoria(null);
                    if (p.getTienda() != null) {
                        p.getTienda().setProductos(null);
                    }
                });
            }

            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            e.printStackTrace(); 
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
        if (catActualizada != null){
            if (catActualizada.getProductos() != null){
                catActualizada.getProductos().forEach(p -> {
                    p.setCategoria(null);
                    if (p.getTienda() != null) {
                        p.getTienda().setProductos(null);
                    }
                });
            }
            return ResponseEntity.ok(catActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente Categoria por su ID", description = "Permite actualizar parcialmente los datos de una categoria existente")
    public ResponseEntity<Categoria> patchCategoria(@PathVariable Long id, @RequestBody Categoria partialCategoria) {
        try {
            Categoria updatedCategoria = categoriaService.patchCategoria(id, partialCategoria);

            if (updatedCategoria.getProductos() != null){
                updatedCategoria.getProductos().forEach(p -> {
                    p.setCategoria(null);
                    if (p.getTienda() != null){
                        p.getTienda().setProductos(null);
                    }
                });
            }
            return ResponseEntity.ok(updatedCategoria);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Categoria por su ID", description = "Elimina una categoria existente")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            categoriaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
