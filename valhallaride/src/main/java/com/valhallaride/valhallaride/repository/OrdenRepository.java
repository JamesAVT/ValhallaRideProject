package com.valhallaride.valhallaride.repository;

import com.valhallaride.valhallaride.model.MetodoPago;
import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {

    List<Orden> findByUsuario_IdUsuario(Integer idUsuario);

    List<Orden> findByPagado(Boolean pagado);

    List<Orden> findByMetodoPago(MetodoPago metodoPago);

    List<Orden> findByUsuario(Usuario usuario);

    // Query 2 - 3 tablas
    @Query("SELECT o.idOrden, o.fecha, u.nombreUsuario, m.nomMetPago " +
       "FROM Orden o " +
       "JOIN o.usuario u " +
       "JOIN o.metodoPago m")
    List<Object[]> listarOrdenesConUsuarioYMetodoPago();

}
