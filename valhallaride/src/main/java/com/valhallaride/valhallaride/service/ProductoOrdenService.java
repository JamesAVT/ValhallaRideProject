package com.valhallaride.valhallaride.service;

import com.valhallaride.valhallaride.model.Producto;
import com.valhallaride.valhallaride.model.ProductoOrden;
import com.valhallaride.valhallaride.repository.OrdenRepository;
import com.valhallaride.valhallaride.repository.ProductoOrdenRepository;
import com.valhallaride.valhallaride.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoOrdenService {

    @Autowired
    private ProductoOrdenRepository productoOrdenRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Transactional
    public ProductoOrden save(ProductoOrden productoOrden) {
        // Asigna fecha si es null
        if (productoOrden.getFechaHora() == null) {
            productoOrden.setFechaHora(LocalDateTime.now());
        }

        // Aquí cargamos el producto completo desde la base de datos
        if (productoOrden.getProducto() != null && productoOrden.getProducto().getIdProducto() != null) {
            Producto producto = productoRepository.findById(
                    productoOrden.getProducto().getIdProducto()).orElse(null);
            
            if (producto != null) {
                productoOrden.setProducto(producto);

                // Aqui calculamos el precio total (cantidad * precio unitario del producto)
                Integer cantidad = productoOrden.getCantidad();
                Integer precioUnitario = producto.getPrecioProducto();

                if (cantidad != null && precioUnitario != null) {
                    Integer precioFinal = cantidad * precioUnitario;
                    productoOrden.setPrecioProducto(precioFinal);
                }
            }
        }

        // Aquí cargamos la orden completa
        if (productoOrden.getOrden() != null && productoOrden.getOrden().getIdOrden() != null) {
            productoOrden.setOrden(
                ordenRepository.findById(productoOrden.getOrden().getIdOrden()).orElse(null)
            );
        }

        return productoOrdenRepository.save(productoOrden);
    }
    

    public List<ProductoOrden> findAll() {
        return productoOrdenRepository.findAll();
    }

    public ProductoOrden findById(Integer id) {
        return productoOrdenRepository.findById(id).orElse(null);
    }

    public List<ProductoOrden> findByOrden(Integer idOrden) {
        return productoOrdenRepository.findByOrden_IdOrden(idOrden);
    }

    public List<ProductoOrden> findByProducto(Integer idProducto) {
        return productoOrdenRepository.findByProducto_IdProducto(idProducto);
    }

    public List<ProductoOrden> findAllOrderByFechaHoraDesc() {
        return productoOrdenRepository.findAllByOrderByFechaHoraDesc();
    }
    
    // Query 3 - 4 tablas
    public List<Object[]> listarProductoOrdenConProductoYUsuario() {
        return productoOrdenRepository.listarProductoOrdenConProductoYUsuario();
    }

    public void delete(Integer id) {
        ProductoOrden productoOrden = productoOrdenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto Orden no encontrada")); 

        productoOrdenRepository.delete(productoOrden);
    }

    public ProductoOrden patchProductoOrden(Integer id, ProductoOrden parcialProductoOrden) {
        Optional<ProductoOrden> productoOrdenOptional = productoOrdenRepository.findById(id);
        if(productoOrdenOptional.isPresent()){

            ProductoOrden productoOrdenToUpdate = productoOrdenOptional.get();

            if (parcialProductoOrden.getCantidad() != null){
                productoOrdenToUpdate.setCantidad(parcialProductoOrden.getCantidad());
            }

            if (parcialProductoOrden.getPrecioProducto() != null){
                productoOrdenToUpdate.setPrecioProducto(parcialProductoOrden.getPrecioProducto());
            }

            if (parcialProductoOrden.getFechaHora() != null){
                productoOrdenToUpdate.setFechaHora(parcialProductoOrden.getFechaHora());
            }

            return productoOrdenRepository.save(productoOrdenToUpdate);
        }else{
            return null;
        }
    }
}
