package com.valhallaride.valhallaride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.valhallaride.valhallaride.model.Categoria;
import com.valhallaride.valhallaride.model.Producto;
import com.valhallaride.valhallaride.model.Tienda;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Producto> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT p FROM Producto p WHERE p.nombreProducto = :nombre AND p.categoria.idCategoria = :idCategoria")
    List<Producto> buscarPorNombreYCategoria(@Param("nombre") String nombre, @Param("idCategoria") Integer idCategoria);

    @Query("SELECT p FROM Producto p WHERE p.precioProducto BETWEEN :min AND :max")
    List<Producto> buscarPorRangoDePrecio(@Param("min") Integer min, @Param("max") Integer max);

    @Query("SELECT p FROM Producto p WHERE p.tienda.idTienda = :idTienda ORDER BY p.precioProducto DESC")
    List<Producto> buscarPorTiendaOrdenadoPorPrecio(@Param("idTienda") Integer idTienda);

    @Query("SELECT p FROM Producto p WHERE p.stockProducto < :stock")
    List<Producto> buscarConStockBajo(@Param("stock") Integer stock);

    List<Producto> findByCategoria(Categoria categoria);

    List<Producto> findByTienda(Tienda tienda);

}
