package com.valhallaride.valhallaride.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.model.Producto;
import com.valhallaride.valhallaride.model.ProductoOrden;
import com.valhallaride.valhallaride.repository.ProductoOrdenRepository;

@ActiveProfiles("test")
@SpringBootTest
public class ProductoOrdenServiceTest {

    @Autowired
    private ProductoOrdenService productoOrdenService;

    @MockBean
    private ProductoOrdenRepository productoOrdenRepository;

    private ProductoOrden createProductoOrden() {
        return new ProductoOrden(
            1,
            2, 
            15000, 
            LocalDateTime.now(), 
            new Orden(),            // Chicos, recordar que aqui se crea un objeto Orden vacio 
            new Producto()          // Y aqui tambien lo mismo pero en Producto 
        );
    }

    @Test
    public void testFindAll() {
        when(productoOrdenRepository.findAll()).thenReturn(List.of(createProductoOrden()));
        List<ProductoOrden> productoOrdenes = productoOrdenService.findAll();
        assertNotNull(productoOrdenes);
        assertEquals(1, productoOrdenes.size());
    }

    @Test
    public void testFindById() {
        when(productoOrdenRepository.findById(1)).thenReturn(java.util.Optional.of(createProductoOrden()));
        ProductoOrden productoOrden = productoOrdenService.findById(1);
        assertNotNull(productoOrden);
        assertEquals(1, productoOrden.getIdProductoOrden());
    }

    @Test
    public void testSave() {
        ProductoOrden productoOrden = createProductoOrden();
        when(productoOrdenRepository.save(productoOrden)).thenReturn(productoOrden);
        ProductoOrden savedProductoOrden = productoOrdenService.save(productoOrden);
        assertNotNull(savedProductoOrden);
        assertEquals(1, savedProductoOrden.getIdProductoOrden());
    }

    @Test
    public void testPatchProductoOrden() {
        ProductoOrden existingProductoOrden = createProductoOrden();
        ProductoOrden patchData = new ProductoOrden();
        patchData.setCantidad(3);

        when(productoOrdenRepository.findById(1)).thenReturn(java.util.Optional.of(existingProductoOrden));
        when(productoOrdenRepository.save(any(ProductoOrden.class))).thenReturn(existingProductoOrden);

        ProductoOrden patchedProductoOrden = productoOrdenService.patchProductoOrden(1, patchData);
        assertNotNull(patchedProductoOrden);
        assertEquals(3, patchedProductoOrden.getCantidad());
    }

    @Test
    public void testDeleteById() {
        ProductoOrden productoOrden = createProductoOrden();

        // Simula que al buscar por ID, encuentra el producto orden
        when(productoOrdenRepository.findById(1)).thenReturn(Optional.of(productoOrden));
        // Simula que al eliminar por ID, no pasa nada (osea, éxito)
        doNothing().when(productoOrdenRepository).delete(productoOrden);

        productoOrdenService.delete(1); // Llama al método a probar

        // Aquí verifica que se llamó a findById y delete correctamente
        verify(productoOrdenRepository, times(1)).findById(1);
        verify(productoOrdenRepository, times(1)).delete(productoOrden);
    }
}
