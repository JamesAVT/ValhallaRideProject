package com.valhallaride.valhallaride.repository;

import com.valhallaride.valhallaride.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {

    List<Orden> findByUsuario_IdUsuario(Integer idUsuario);

    List<Orden> findByPagado(Boolean pagado);
    
    List<Orden> findByMetodoPago(MetodoPago metodopago);
    
}
