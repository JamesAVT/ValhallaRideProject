package com.valhallaride.valhallaride.controller;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valhallaride.valhallaride.assemblers.UsuarioModelAssembler;
import com.valhallaride.valhallaride.model.Usuario;
import com.valhallaride.valhallaride.service.UsuarioService;

@RestController
@RequestMapping("/api/v2/usuarios")
public class UsuarioControllerv2 {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Usuario>> getAllUsuarios(){ // Método para obtener todos los usuarios 
        List<EntityModel<Usuario>> usuarios = usuarioService.findAll().stream()
            .map(assembler::toModel) // Convierte cada usuario en un EntityModel
            .collect(Collectors.toList()); //  Y obtenemos sus links de HATEOAS
        return CollectionModel.of(usuarios, // Se "guarda" la lista en el CollectionModel, con enlaces HATEOAS
                linkTo(methodOn(UsuarioControllerv2.class).getAllUsuarios()).withSelfRel()); 
        
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Usuario> getUsuarioById(@PathVariable Long id){
        Usuario usuario = usuarioService.findById(id); // Se busca el usuario usando service
        return assembler.toModel(usuario); // Se convierte el Usuario en EntityModel, usando el assembler(agrega enlaces HATEOAS)
    }

    // Query 4 Usuarios con su rol y total de órdenes realizadas
    @GetMapping("/usuarios-detallados")
    public ResponseEntity<List<Map<String, Object>>> obtenerUsuariosDetallados() {
        return ResponseEntity.ok(usuarioService.listarUsuariosConRolYOrdenes());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> crearUsuario(@RequestBody Usuario usuario){
        Usuario nuevoUsuario = usuarioService.save(usuario); // Se guarda el nuevo usuario usando service
        return ResponseEntity
                .ok(assembler.toModel(nuevoUsuario)); // Devuelve respuesta 200 OK con el usuario con EntityModel (con enlaces HATEOAS)

    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario){
        usuario.setIdUsuario(id); // Asigna el ID recibido 
        Usuario usuarioActualizado = usuarioService.save(usuario); // Guarda el usuario actualizado usando service
        return ResponseEntity // Devuelve una respuesta HTTP 200 OK
                .ok(assembler.toModel(usuarioActualizado)); // Con EntityModel para incluir enlaces HATEOAS

    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<?> eliminarUsuario(@PathVariable Long id){
        usuarioService.delete(id); // Llama a service para eliminar el usuaio con el ID recibido
        return ResponseEntity.noContent().build(); // Devuelve respuesta 204 No Content
    }
}
