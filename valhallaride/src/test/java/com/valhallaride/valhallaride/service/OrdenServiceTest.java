package com.valhallaride.valhallaride.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.valhallaride.valhallaride.model.MetodoPago;
import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.model.Usuario;
import com.valhallaride.valhallaride.repository.OrdenRepository;

@ActiveProfiles("test")
@SpringBootTest
public class OrdenServiceTest {

    @Autowired
    private OrdenService ordenService;

    @MockBean
    private OrdenRepository ordenRepository;

    private Orden createOrden() {
        return new Orden(
            1,
            LocalDate.now(),
            false, 
            new Usuario(),      // Objeto vacio de usuario
            new ArrayList<>(), //  Lista vacia de ProductoOrden
            new MetodoPago()  //   Objeto vacio de metodopago
        );
    }

    @Test
    public void testFindAll() {
        when(ordenRepository.findAll()).thenReturn(List.of(createOrden()));
        List<Orden> ordenes = ordenService.findAll();
        assertNotNull(ordenes);
        assertEquals(1, ordenes.size());
    }

    @Test
    public void testFindById() {
        when(ordenRepository.findById(1)).thenReturn(java.util.Optional.of(createOrden()));
        Orden orden = ordenService.findById(1);
        assertNotNull(orden);
        assertEquals(1, orden.getIdOrden());
    }

    @Test
    public void testSave() {
        Orden orden = createOrden();
        when(ordenRepository.save(orden)).thenReturn(orden);
        Orden savedOrden = ordenService.save(orden);
        assertNotNull(savedOrden);
        assertEquals(1, savedOrden.getIdOrden());
    }

    @Test
    public void testPatchOrden() {
        Orden existingOrden = createOrden();
        Orden patchData = new Orden();
        patchData.setPagado(true);

        when(ordenRepository.findById(1)).thenReturn(java.util.Optional.of(existingOrden));
        when(ordenRepository.save(any(Orden.class))).thenReturn(existingOrden);

        Orden patchedOrden = ordenService.patchOrden(1, patchData);
        assertNotNull(patchedOrden);
        assertEquals(true, patchedOrden.getPagado()); // Chicos, recordar que aquí en patch se espera que haya un cambio de valor que 
                                                               // estamos actualizando. En este caso, se espera que pagado pase de "false" a "true"
    }

    @Test
    public void testDeleteById() {
        Orden orden = createOrden();

        // Simula que al buscar por ID, encuentra la orden
        when(ordenRepository.findById(1)).thenReturn(Optional.of(orden));
        // Simula que al eliminar por ID, no pasa nada (osea, éxito)
        doNothing().when(ordenRepository).delete(orden);

        ordenService.delete(1); // Llama al método a probar

        // Aquí verifica que se llamó a findById y delete correctamente
        verify(ordenRepository, times(1)).findById(1);
        verify(ordenRepository, times(1)).delete(orden);
    }
}
