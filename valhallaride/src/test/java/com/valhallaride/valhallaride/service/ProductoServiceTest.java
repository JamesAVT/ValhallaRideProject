package com.valhallaride.valhallaride.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.valhallaride.valhallaride.model.Categoria;
import com.valhallaride.valhallaride.model.Producto;
import com.valhallaride.valhallaride.model.Tienda;
import com.valhallaride.valhallaride.repository.ProductoRepository;

@ActiveProfiles("test")
@SpringBootTest
public class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    @MockBean
    private ProductoRepository productoRepository;

    private Producto createProducto() {
        return new Producto(
            1, 
            "Casco Motocross", 
            "Casco con ultra protección para motocross", 
            45000, 
            10, 
            new Categoria(),    // Objeto vacio de categoria
            new Tienda()        // Objeto vacio de tienda
        );
    }

    @Test
    public void testFindAll() {
        when(productoRepository.findAll()).thenReturn(List.of(createProducto()));
        List<Producto> productos = productoService.findAll();
        assertNotNull(productos);
        assertEquals(1, productos.size());
    }

    @Test
    public void testFindById() {
        when(productoRepository.findById(1)).thenReturn(java.util.Optional.of(createProducto()));
        Producto producto = productoService.findById(1);
        assertNotNull(producto);
        assertEquals("Casco Motocross", producto.getNombreProducto());
    }

    @Test
    public void testSave() {
        Producto producto = createProducto();
        when(productoRepository.save(producto)).thenReturn(producto);
        Producto savedProducto = productoService.save(producto);
        assertNotNull(savedProducto);
        assertEquals("Casco Motocross", savedProducto.getNombreProducto());
    }

    @Test
    public void testPatchProducto() {
        Producto existingProducto = createProducto();
        Producto patchData = new Producto();
        patchData.setNombreProducto("Casco Enduro Fox V1");

        when(productoRepository.findById(1)).thenReturn(java.util.Optional.of(existingProducto));
        when(productoRepository.save(any(Producto.class))).thenReturn(existingProducto);

        Producto patchedProducto = productoService.patchProducto(1, patchData);
        assertNotNull(patchedProducto);
        assertEquals("Casco Enduro Fox V1", patchedProducto.getNombreProducto());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(productoRepository).deleteById(1);
        productoService.delete(1);
        verify(productoRepository, times(1)).deleteById(1);
    }
}
