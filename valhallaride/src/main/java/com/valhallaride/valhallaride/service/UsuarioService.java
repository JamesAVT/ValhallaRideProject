package com.valhallaride.valhallaride.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.model.ProductoOrden;
import com.valhallaride.valhallaride.model.Usuario;
import com.valhallaride.valhallaride.repository.OrdenRepository;
import com.valhallaride.valhallaride.repository.ProductoOrdenRepository;
import com.valhallaride.valhallaride.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private ProductoOrdenRepository productoOrdenRepository;

    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id){
        return usuarioRepository.findById(id).orElse(null);
    }

    // Query 4  Usuarios con su rol y total de órdenes realizadas
    public List<Object[]> listarUsuariosConRolYTotalOrdenes() {
        return usuarioRepository.listarUsuariosConRolYTotalOrdenes();
    }


    public Usuario save(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id){
        // Aqui verificamos si el usuario existe
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Aca hacemos un listado de ordenes asociados al usuario
        List<Orden> ordenes = ordenRepository.findByUsuario(usuario);

        for (Orden orden : ordenes) {
            // Aca buscamos los ProductoOrden de cada orden 
            List<ProductoOrden> productosOrden = productoOrdenRepository.findByOrden(orden);

            // Aca eliminamos todos los ProductoOrden de la orden
            for (ProductoOrden productoOrden : productosOrden) {
                productoOrdenRepository.delete(productoOrden);
            }

            // Aca se elimina la orden
            ordenRepository.delete(orden);
        }

        // Y aqui eliminamos el usuario
        usuarioRepository.delete(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario usuario) {
        Usuario usuarioToUpdate = usuarioRepository.findById(id).orElse(null);
        
        if (usuarioToUpdate != null) {
            usuarioToUpdate.setNombreUsuario((usuario.getNombreUsuario()));
            usuarioToUpdate.setCorreoUsuario(usuario.getCorreoUsuario());
            usuarioToUpdate.setContrasena(usuario.getContrasena());
            usuarioToUpdate.setRol(usuario.getRol());

            return usuarioRepository.save(usuarioToUpdate);
        } else {
            return null;
        }
    }

    public Usuario patchUsuario(Long id, Usuario parcialUsuario){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()){

            Usuario usuarioToUpdate = usuarioOptional.get();

            if (parcialUsuario.getNombreUsuario() != null){
                usuarioToUpdate.setNombreUsuario(parcialUsuario.getNombreUsuario());
            }
            
            if (parcialUsuario.getCorreoUsuario() != null){
                usuarioToUpdate.setCorreoUsuario(parcialUsuario.getCorreoUsuario());
            }

            if (parcialUsuario.getContrasena() != null){
                usuarioToUpdate.setContrasena(parcialUsuario.getContrasena());
            }

            return usuarioRepository.save(usuarioToUpdate);
        } else{
            return null;
        }
    }
}
