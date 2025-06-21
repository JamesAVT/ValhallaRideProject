package com.valhallaride.valhallaride.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valhallaride.valhallaride.model.Categoria;
import com.valhallaride.valhallaride.model.Producto;
import com.valhallaride.valhallaride.model.ProductoOrden;
import com.valhallaride.valhallaride.repository.CategoriaRepository;
import com.valhallaride.valhallaride.repository.ProductoOrdenRepository;
import com.valhallaride.valhallaride.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoOrdenRepository productoOrdenRepository;

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void delete(Long id) {
        // Aca verificamos si la categoria existe
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        
        // Y aca hacemos un listado de productos asociados a la categoria
        List<Producto> productos = productoRepository.findByCategoria(categoria);

        for (Producto producto : productos) {
            // Aqui se eliminan todos los ProductoOrden que esten asociados a el producto
            List<ProductoOrden> productoOrdenes = productoOrdenRepository.findByProducto(producto);
            for (ProductoOrden productoOrden : productoOrdenes) {
                productoOrdenRepository.delete(productoOrden);
            }

            // Aqui se elimina el producto
            productoRepository.delete(producto);
        }
        
        // Y aqui se elimina la categoria
        categoriaRepository.delete(categoria);
    }

    public Categoria updateCategoria(Long id, Categoria categoria){
        Categoria categoriaToUpdate = categoriaRepository.findById(id).orElse(null);
        if (categoriaToUpdate != null) {
            categoriaToUpdate.setNombreCategoria(categoria.getNombreCategoria());
            return categoriaRepository.save(categoriaToUpdate);
        } else {
            return null;
        }
    }
    

    public Categoria patchCategoria(Long id, Categoria parcialCategoria) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if (categoriaOptional.isPresent()) {

            Categoria categoriaToUpdate = categoriaOptional.get();

            if (parcialCategoria.getNombreCategoria() != null) {
                categoriaToUpdate.setNombreCategoria(parcialCategoria.getNombreCategoria());
            }

            return categoriaRepository.save(categoriaToUpdate);
        } else {
            return null;
        }
    }
}
