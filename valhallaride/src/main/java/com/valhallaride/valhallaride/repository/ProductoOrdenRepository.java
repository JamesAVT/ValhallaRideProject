package com.valhallaride.valhallaride.repository;

import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.model.Producto;
import com.valhallaride.valhallaride.model.ProductoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface ProductoOrdenRepository extends JpaRepository<ProductoOrden, Integer> {

    // Buscar por ID de orden
    List<ProductoOrden> findByOrden_IdOrden(Integer idOrden);

    // Buscar por ID de producto
    List<ProductoOrden> findByProducto_IdProducto(Integer idProducto);

    // Buscar todas las ventas (producto + fecha)
    List<ProductoOrden> findAllByOrderByFechaHoraDesc();

    List<ProductoOrden> findByOrden(Orden orden);

    List<ProductoOrden> findByProducto(Producto producto);

    // Query 3 - 4 tablas
    @Query("SELECT po.idProductoOrden, p.nombreProducto, po.cantidad, u.nombreUsuario " +
       "FROM ProductoOrden po JOIN po.producto p JOIN po.orden o JOIN o.usuario u")
    List<Object[]> listarDetalleProductoOrden();
    // 
    List<ProductoOrden> findByProducto_IdProductoAndOrden_IdOrdenAndCantidadGreaterThanEqual(Integer idProducto, Integer idOrden, Integer cantidad);

}
