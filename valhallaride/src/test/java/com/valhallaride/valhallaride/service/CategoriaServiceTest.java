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

import com.valhallaride.valhallaride.model.Categoria;
import com.valhallaride.valhallaride.repository.CategoriaRepository;

@ActiveProfiles("test")
@SpringBootTest
public class CategoriaServiceTest {

    @Autowired
    private CategoriaService categoriaService;

    @MockBean
    private CategoriaRepository categoriaRepository;

    private Categoria createCategoria() {
        return new Categoria(
            1, 
            "Motocross", 
            new ArrayList<>()   // Creamos una lista vacia de List<Producto>, evitando asi que quede null
        );
    }

    @Test
    public void testFindAll() {
        when(categoriaRepository.findAll()).thenReturn(List.of(createCategoria()));
        List<Categoria> categorias = categoriaService.findAll();
        assertNotNull(categorias);
        assertEquals(1, categorias.size());
    }

    @Test
    public void testFindById() {
        when(categoriaRepository.findById(1L)).thenReturn(java.util.Optional.of(createCategoria()));
        Categoria categoria = categoriaService.findById(1L);
        assertNotNull(categoria);
        assertEquals("Motocross", categoria.getNombreCategoria());
    }

    @Test
    public void testSave() {
        Categoria categoria = createCategoria();
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        Categoria savedCategoria = categoriaService.save(categoria);
        assertNotNull(savedCategoria);
        assertEquals("Motocross", savedCategoria.getNombreCategoria());
    }

    @Test
    public void testPatchCategoria() {
        Categoria existingCategoria = createCategoria();
        Categoria patchData = new Categoria();
        patchData.setNombreCategoria("Racing");

        when(categoriaRepository.findById(1L)).thenReturn(java.util.Optional.of(existingCategoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(existingCategoria);

        Categoria patchedCategoria = categoriaService.patchCategoria(1L, patchData);
        assertNotNull(patchedCategoria);
        assertEquals("Racing", patchedCategoria.getNombreCategoria());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(categoriaRepository).deleteById(1L);
        categoriaService.delete(1L);
        verify(categoriaRepository, times(1)).deleteById(1L);
    }

}

