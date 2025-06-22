package com.valhallaride.valhallaride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.valhallaride.valhallaride.model.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;


public interface  UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    List<Usuario> findByNombreUsuario(String nombreUsuario); // Chicos! Este devuelve una lista porque puede haber varios usuarios con el mismo nombre
    Optional<Usuario> findByCorreoUsuario(String correoUsuario); // En este caso, usamos "Optional", ya que el correo es unico, y tambien esta la posibilidad de que no exista
    List<Usuario> findByRol_idRol(Integer idRol); // Aqui buscamos todos los usuarios que tengan un rol específico (por el id de rol)

    // Query 4 - 3 tablas Usuarios con su rol y total de órdenes realizadas
    @Query("SELECT u.nombreUsuario, r.nombreRol, COUNT(o) " +
       "FROM Usuario u " +
       "JOIN u.rol r " +
       "LEFT JOIN u.ordenes o " +
       "GROUP BY u.nombreUsuario, r.nombreRol")
    List<Object[]> listarUsuariosConRolYTotalOrdenes();

}
