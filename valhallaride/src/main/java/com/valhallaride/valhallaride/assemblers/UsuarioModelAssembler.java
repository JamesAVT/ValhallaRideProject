package com.valhallaride.valhallaride.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.valhallaride.valhallaride.controller.UsuarioControllerv2;
import com.valhallaride.valhallaride.model.Usuario;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override // Sobrescribe el método toModel()
    public EntityModel<Usuario> toModel(Usuario usuario) { // Convierte un objeto "Usuario" en un EntityModel (Enlaces HATEOAS)
        return EntityModel.of(usuario, // Se crea el EntityModel
            // Enlace a este mismo usuario(self), osea, obtener un usuario por ID
            linkTo(methodOn(UsuarioControllerv2.class).getUsuarioById(Long.valueOf(usuario.getIdUsuario()))).withSelfRel(),
            // Enlace para obtener todos los usuarios
            linkTo(methodOn(UsuarioControllerv2.class).getAllUsuarios()).withRel("usuarios"),
            // Enlace para crear un usuario
            linkTo(methodOn(UsuarioControllerv2.class).crearUsuario(usuario)).withRel("crear_usuarios"),
            // Enlace para actualizar un usuario
            linkTo(methodOn(UsuarioControllerv2.class).actualizarUsuario(Long.valueOf(usuario.getIdUsuario()), usuario)).withRel("actualizar_usuarios"),
            // Enlace para eliminar un usuario
            linkTo(methodOn(UsuarioControllerv2.class).eliminarUsuario(Long.valueOf(usuario.getIdUsuario()))).withRel("eliminar_usuario"));

    }
}
