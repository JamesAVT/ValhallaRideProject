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
import java.util.Map;
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
    public ResponseEntity<List<Usuario>> listar(){ // Método para obtener la lista de todos los usuarios
        List <Usuario> usuarios = usuarioService.findAll(); // Llama a service para obtener todos los usuarios 
        if(usuarios.isEmpty()){ // Si no hay usuarios
            return ResponseEntity.noContent().build(); // Devuelve respuesta 204 No Content
        }
        // Evita bucles
        for (Usuario u : usuarios) {
            if (u.getOrdenes() != null){ // Si el usuario tiene órdenes
                u.getOrdenes().forEach(o -> o.setUsuario(null)); // A cada orden, le quita la referencia al usuario (o.setUsuario(null))
            }

            if (u.getRol() != null && u.getRol().getUsuario() != null) { // Si el usuario tiene un rol
                u.getRol().setUsuario(null); // Y ese rol tiene de vuelta la referencia al usuario, también elimna(rol.setUsuario(null))
            }
        }
        return ResponseEntity.ok(usuarios); // Devuelve 200 OK
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un Usuario por su ID", description = "Obtiene un usuario existente")
    public ResponseEntity<Usuario> buscar(@PathVariable Long id){ // Método para buscar un usuario por su ID
        try{ // Intenta buscar el usuario usando service
            Usuario usuario = usuarioService.findById(id);
            if (usuario == null) { // Si el usuario no existe
                return ResponseEntity.notFound().build(); // Responde con 404 Not Found
            }

            if (usuario.getOrdenes() != null) { // Si el usuario tiene órdenes
                usuario.getOrdenes().forEach(o -> o.setUsuario(null)); // Se eliminan las referencias desde las órdenes hacia el usuario, para evitar bucles
            }

            if (usuario.getRol() != null && usuario.getRol().getUsuario() != null) { 
                usuario.getRol().setUsuario(null); // Si rol existe y tiene referencia de vuelta al usuario, se limpia
            }

            return ResponseEntity.ok(usuario); // Devuelve el usuario encontrado, con 200 OK
        }catch (Exception e){ // Y si hay excepción
            return ResponseEntity.notFound().build(); // Responde con 404 Not Found
        }
    }

    // Query 4 Usuarios con su rol y total de órdenes realizadas
    @GetMapping("/usuarios-detallados")
    @Operation(summary = "Usuarios con su rol y cantidad de ordenes", description = "Obtiene usuarios junto con su rol y número de órdenes realizadas")
    public ResponseEntity<List<Map<String, Object>>> obtenerUsuariosDetallados() {
        return ResponseEntity.ok(usuarioService.listarUsuariosConRolYOrdenes());
    }


    @PostMapping
    @Operation(summary = "Crear Usuario", description = "Crea un nuevo usuario")
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario){ // Método para crear un nuevo usuario
        Usuario usuarioNuevo = usuarioService.save(usuario); // Llama a service para guardar el nuevo usuario
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo); // Devuelve un 201 Created y el objeto "usuarioNuevo"
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Usuario por su ID", description = "Actualiza todos los datos de un usuario existente")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario){ // Método para actualizar un usuario existente por su ID
        Usuario usuarioActualizado = usuarioService.updateUsuario(id, usuario); // Llama a service para actualizar el usuario con esa ID
        if (usuarioActualizado != null){ // Si la actualización fue exitosa
            return ResponseEntity.ok(usuarioActualizado); // Responde con 200 OK
        } else { //Y si no se encontró el usuario
            return ResponseEntity.notFound().build(); // Responde con 404 Not Found
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un Usuario por su ID", description = "Actualiza parcialmente los datos de un usuario existente")
    public ResponseEntity<Usuario> patchUsuario(@PathVariable Long id, @RequestBody Usuario partialUsuario){ // Método para actualizar parcialmente un usuario por su ID
        try{
            Usuario updatedUsuario = usuarioService.patchUsuario(id, partialUsuario); // Intenta aplicar la actualización parcial usando service
            if (updatedUsuario == null) { // Si no se encuentra el usuario a actualizar
                return ResponseEntity.notFound().build(); // Responde con 404 Not Found
            }
            // Se limpian referencias inversas para evitar bucles
            if (updatedUsuario.getOrdenes() != null){ // Si el usuario actualizado tiene una lista de órdenes asociadas
                updatedUsuario.getOrdenes().forEach(o -> o.setUsuario(null)); // Recorre cada orden y le quitamos la referencia al usuario
            }

            if (updatedUsuario.getRol() != null && updatedUsuario.getRol().getUsuario() != null){ // Si el usuario tiene un rol asignado y ese rol tiene referencia de vuelta al usuario
                updatedUsuario.getRol().setUsuario(null); // Quita esa referencia al usuario desde rol
            }
            
            return ResponseEntity.ok(updatedUsuario); // Devuelve el usuario actualizado con 200 OK
        }catch (RuntimeException e) { // Y si ocurre una excepción
            return ResponseEntity.notFound().build(); // Responde con 404 Not Found
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Usuario por su ID", description = "Elimina un usuario existente")
    public ResponseEntity<?> eliminar(@PathVariable Long id){ // Método para eliminar usuario por ID
        try{
            usuarioService.delete(id); // Se intenta eliminar el usuario desde service
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        }catch (Exception e){ // Y si hay una excepción
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found
        }
    }
}
