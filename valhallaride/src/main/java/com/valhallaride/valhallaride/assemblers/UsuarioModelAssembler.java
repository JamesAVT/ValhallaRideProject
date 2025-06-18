package com.valhallaride.valhallaride.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.valhallaride.valhallaride.controller.UsuarioControllerv2;
import com.valhallaride.valhallaride.model.Usuario;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioControllerv2.class).getUsuarioById(Long.valueOf(usuario.getIdUsuario()))).withSelfRel(),
            linkTo(methodOn(UsuarioControllerv2.class).getAllUsuarios()).withRel("usuarios"),
            linkTo(methodOn(UsuarioControllerv2.class).crearUsuario(usuario)).withRel("crear_usuarios"),
            linkTo(methodOn(UsuarioControllerv2.class).actualizarUsuario(Long.valueOf(usuario.getIdUsuario()), usuario)).withRel("actualizar_usuarios"),
            linkTo(methodOn(UsuarioControllerv2.class).eliminarUsuario(Long.valueOf(usuario.getIdUsuario()))).withRel("eliminar_usuario"));

    }
}
