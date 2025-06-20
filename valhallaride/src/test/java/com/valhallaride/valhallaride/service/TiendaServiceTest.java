package com.valhallaride.valhallaride.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.valhallaride.valhallaride.model.Tienda;
import com.valhallaride.valhallaride.repository.TiendaRepository;

@ActiveProfiles("test")
@SpringBootTest
public class TiendaServiceTest {

    @Autowired
    private TiendaService tiendaService;

    @MockBean
    private TiendaRepository tiendaRepository;

    private Tienda createTienda() {
        return new Tienda(
            1L, 
            "ValhallaRide", 
            "Av. Kennedy, Las Condes", 
            new ArrayList<>() // Lista vacia de productos. Usamos ArrayList porque es una lista de productos
        );
    }

    @Test
    public void testFindAll() {
        when(tiendaRepository.findAll()).thenReturn(List.of(createTienda()));
        List<Tienda> tiendas = tiendaService.findAll();
        assertNotNull(tiendas);
        assertEquals(1, tiendas.size());
    }

    @Test
    public void testFindById() {
        when(tiendaRepository.findById(1L)).thenReturn(java.util.Optional.of(createTienda()));
        Tienda tienda = tiendaService.findById(1L);
        assertNotNull(tienda);
        assertEquals("ValhallaRide", tienda.getNombreTienda());
    }

    @Test
    public void testSave() {
        Tienda tienda = createTienda();
        when(tiendaRepository.save(tienda)).thenReturn(tienda);
        Tienda savedTienda = tiendaService.save(tienda);
        assertNotNull(savedTienda);
        assertEquals("ValhallaRide", savedTienda.getNombreTienda());
    }

    @Test
    public void testPatchTienda() {
        Tienda existingTienda = createTienda();
        Tienda patchData = new Tienda();
        patchData.setDireccionTienda("Av Vitacura #9391");

        when(tiendaRepository.findById(1L)).thenReturn(java.util.Optional.of(existingTienda));
        when(tiendaRepository.save(any(Tienda.class))).thenReturn(existingTienda);

        Tienda patchedTienda = tiendaService.patchTienda(1L, patchData);
        assertNotNull(patchedTienda);
        assertEquals("Av Vitacura #9391", patchedTienda.getDireccionTienda());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(tiendaRepository).deleteById(1L);
        tiendaService.delete(1L);
        verify(tiendaRepository, times(1)).deleteById(1L);
    }
}
