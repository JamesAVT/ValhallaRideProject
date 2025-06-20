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

import com.valhallaride.valhallaride.model.MetodoPago;
import com.valhallaride.valhallaride.repository.MetodoPagoRepository;

@ActiveProfiles("test")
@SpringBootTest
public class MetodoPagoServiceTest {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @MockBean
    private MetodoPagoRepository metodoPagoRepository;

    private MetodoPago createMetodoPago() {
        return new MetodoPago(1, "Debito");
    }

    @Test
    public void testFindAll() {
        when(metodoPagoRepository.findAll()).thenReturn(List.of(createMetodoPago()));
        List<MetodoPago> metodoPagos = metodoPagoService.findAll();
        assertNotNull(metodoPagos);
        assertEquals(1, metodoPagos.size());
    }

    @Test
    public void testFindById() {
        when(metodoPagoRepository.findById(1L)).thenReturn(java.util.Optional.of(createMetodoPago()));
        MetodoPago metodoPago = metodoPagoService.findById(1L);
        assertNotNull(metodoPago);
        assertEquals("Debito", metodoPago.getNomMetPago());
    }

    @Test
    public void testSave() {
        MetodoPago metodoPago = createMetodoPago();
        when(metodoPagoRepository.save(metodoPago)).thenReturn(metodoPago);
        MetodoPago savedMetodoPago = metodoPagoService.save(metodoPago);
        assertNotNull(savedMetodoPago);
        assertEquals("Debito", savedMetodoPago.getNomMetPago());
    }

    @Test
    public void testPatchMetodoPago() {
        MetodoPago existingMetodoPago = createMetodoPago();
        MetodoPago patchData = new MetodoPago();
        patchData.setNomMetPago("Efectivo");

        when(metodoPagoRepository.findById(1L)).thenReturn(java.util.Optional.of(existingMetodoPago));
        when(metodoPagoRepository.save(any(MetodoPago.class))).thenReturn(existingMetodoPago);

        MetodoPago patchedMetodoPago = metodoPagoService.patchMetodoPago(1L, patchData);
        assertNotNull(patchedMetodoPago);
        assertEquals("Efectivo", patchedMetodoPago.getNomMetPago());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(metodoPagoRepository).deleteById(1L);
        metodoPagoService.delete(1L);
        verify(metodoPagoRepository, times(1)).deleteById(1L);
    }
}
