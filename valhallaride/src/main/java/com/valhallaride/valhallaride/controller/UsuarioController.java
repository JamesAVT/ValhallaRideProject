package com.valhallaride.valhallaride.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
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

import com.valhallaride.valhallaride.model.Usuario;
import com.valhallaride.valhallaride.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con Usuarios")

public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene una lista de todos los usuarios")
    public ResponseEntity<List<Usuario>> listar(){
        List <Usuario> usuarios = usuarioService.findAll();
        if(usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        for (Usuario u : usuarios) {
            if (u.getOrdenes() != null){
                u.getOrdenes().forEach(o -> o.setUsuario(null));
            }

            if (u.getRol() != null && u.getRol().getUsuario() != null) {
                u.getRol().setUsuario(null);
            }
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un Usuario por su ID", description = "Obtiene un usuario existente")
    public ResponseEntity<Usuario> buscar(@PathVariable Long id){
        try{
            Usuario usuario = usuarioService.findById(id);
            if (usuario == null) {
                return ResponseEntity.notFound().build();
            }

            if (usuario.getOrdenes() != null) {
                usuario.getOrdenes().forEach(o -> o.setUsuario(null));
            }

            if (usuario.getRol() != null && usuario.getRol().getUsuario() != null) {
                usuario.getRol().setUsuario(null);
            }

            return ResponseEntity.ok(usuario);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear Usuario", description = "Crea un nuevo usuario")
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario){
        Usuario usuarioNuevo = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Usuario por su ID", description = "Actualiza todos los datos de un usuario existente")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario){
        Usuario usuarioActualizado = usuarioService.updateUsuario(id, usuario);
        if (usuarioActualizado != null){
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un Usuario por su ID", description = "Actualiza parcialmente los datos de un usuario existente")
    public ResponseEntity<Usuario> patchUsuario(@PathVariable Long id, @RequestBody Usuario partialUsuario){
        try{
            Usuario updatedUsuario = usuarioService.patchUsuario(id, partialUsuario);
            if (updatedUsuario == null) {
                return ResponseEntity.notFound().build();
            }

            if (updatedUsuario.getOrdenes() != null){
                updatedUsuario.getOrdenes().forEach(o -> o.setUsuario(null));
            }

            if (updatedUsuario.getRol() != null && updatedUsuario.getRol().getUsuario() != null){
                updatedUsuario.getRol().setUsuario(null);
            }
            
            return ResponseEntity.ok(updatedUsuario);
        }catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Usuario por su ID", description = "Elimina un usuario existente")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        try{
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
