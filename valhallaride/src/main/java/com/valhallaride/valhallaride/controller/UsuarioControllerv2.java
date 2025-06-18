package com.valhallaride.valhallaride.controller;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
    public CollectionModel<EntityModel<Usuario>> getAllUsuarios(){
        List<EntityModel<Usuario>> usuarios = usuarioService.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioControllerv2.class).getAllUsuarios()).withSelfRel());
        
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Usuario> getUsuarioById(@PathVariable Long id){
        Usuario usuario = usuarioService.findById(id);
        return assembler.toModel(usuario);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> crearUsuario(@RequestBody Usuario usuario){
        Usuario nuevoUsuario = usuarioService.save(usuario);
        return ResponseEntity
                .ok(assembler.toModel(nuevoUsuario));

    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario){
        usuario.setIdUsuario(id.intValue());
        Usuario usuarioActualizado = usuarioService.save(usuario);
        return ResponseEntity
                .ok(assembler.toModel(usuarioActualizado));

    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<?> eliminarUsuario(@PathVariable Long id){
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
