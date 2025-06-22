package com.valhallaride.valhallaride.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valhallaride.valhallaride.model.Producto;
import com.valhallaride.valhallaride.model.ProductoOrden;
import com.valhallaride.valhallaride.repository.ProductoOrdenRepository;
import com.valhallaride.valhallaride.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoOrdenRepository productoOrdenRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto findById(Integer id) {
        return productoRepository.findById(id).orElse(null);
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void delete(Integer id) {
        // Verificamos si el producto existe
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Chicos, aqui hacemos un listado de ProductoOrden asociado al producto
        List<ProductoOrden> productosOrden = productoOrdenRepository.findByProducto(producto);

        // Aqui se elimina los ProductoOrden asociados al producto
        for (ProductoOrden productoOrden : productosOrden) {
            productoOrdenRepository.delete(productoOrden);
        }

        // Y aqui se elimina el producto
        productoRepository.delete(producto);
    }

    public Producto updateProducto(Integer id, Producto producto) {
        Producto productoToUpdate = productoRepository.findById(id).orElse(null);
        if (productoToUpdate != null) {
            productoToUpdate.setNombreProducto(producto.getNombreProducto());
            productoToUpdate.setDescripcionProducto(producto.getDescripcionProducto());
            productoToUpdate.setPrecioProducto(producto.getPrecioProducto());
            productoToUpdate.setPrecioProducto(producto.getPrecioProducto());
            return productoRepository.save(productoToUpdate);
        } else {
            return null;
        }
    }

    public Producto patchProducto(Integer id, Producto parcialProducto) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isPresent()) {

            Producto productoToUpdate = productoOptional.get();

            if (parcialProducto.getNombreProducto() != null) {
                productoToUpdate.setNombreProducto(parcialProducto.getNombreProducto());
            }

            if (parcialProducto.getDescripcionProducto() != null) {
                productoToUpdate.setDescripcionProducto(parcialProducto.getDescripcionProducto());
            }

            if (parcialProducto.getPrecioProducto() != null) {
                productoToUpdate.setPrecioProducto(parcialProducto.getPrecioProducto());
            }

            if (parcialProducto.getStockProducto() != null) {
                productoToUpdate.setStockProducto(parcialProducto.getStockProducto());
            }

            return productoRepository.save(productoToUpdate);
        } else {
            return null;
        }
    }

    // Buscar productos por nombre (contiene, sin distinguir mayúsculas)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.buscarPorNombre(nombre);
    }

    // Buscar productos por nombre y categoría
    public List<Producto> buscarPorNombreYCategoria(String nombre, Integer idCategoria) {
        return productoRepository.buscarPorNombreYCategoria(nombre, idCategoria);
    }

    // Buscar productos en un rango de precios
    public List<Producto> buscarPorRangoDePrecio(Integer min, Integer max) {
        return productoRepository.buscarPorRangoDePrecio(min, max);
    }

    // Buscar productos por tienda ordenados por precio descendente
    public List<Producto> buscarPorTiendaOrdenadoPorPrecio(Integer idTienda) {
        return productoRepository.buscarPorTiendaOrdenadoPorPrecio(idTienda);
    }

    // Buscar productos con stock bajo
    public List<Producto> buscarConStockBajo(Integer stock) {
        return productoRepository.buscarConStockBajo(stock);
    }
    
    // Query 1 - 3 tablas
    public List<Object[]> listarProductosConCategoriaYTienda() {
        return productoRepository.listarProductosConCategoriaYTienda();
    }
}
