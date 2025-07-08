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
import java.util.Optional;

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
            1L, 
            "Motocross", 
            new ArrayList<>()   // Creamos una lista vacia de List<Producto>, evitando asi que quede null
        );
    }

    @Test
    public void testFindAll() { // garantizamos que la funcionalidad de consulta de categorías esté funcionando correctamente
        when(categoriaRepository.findAll()).thenReturn(List.of(createCategoria()));
        List<Categoria> categorias = categoriaService.findAll();
        assertNotNull(categorias); // aqui verificamos que la lista no sea null
        assertEquals(1, categorias.size());//verificamos que la lista tenga 1 elemento
    }

    @Test
    public void testFindById() { 
        when(categoriaRepository.findById(1L)).thenReturn(java.util.Optional.of(createCategoria()));// cuando se llame a findById(1L), devuelva una categoría existente
        Categoria categoria = categoriaService.findById(1L);
        assertNotNull(categoria); // Asegura que la categoría no sea null
        assertEquals("Motocross", categoria.getNombreCategoria()); // Valida que el nombre sea correcto
    }

    @Test
    public void testSave() {  
        Categoria categoria = createCategoria(); // Crea una instancia ficticia de categoria que devuelve una categoría predefinida con datos
        when(categoriaRepository.save(categoria)).thenReturn(categoria); // devuelve la misma categoría como si hubiera sido guardada correctamente
        Categoria savedCategoria = categoriaService.save(categoria);
        assertNotNull(savedCategoria); 
        assertEquals("Motocross", savedCategoria.getNombreCategoria()); //Comprueba que el nombre de la categoría sea guardada
    }

    @Test
    public void testPatchCategoria() {
        Categoria existingCategoria = createCategoria(); // simulamos una categoria preexistente, que representa un registro que ya está en la base de datos 
        Categoria patchData = new Categoria(); // Crea una instancia vacia de Categoria que actua como un parche
        patchData.setNombreCategoria("Racing");  // modifica campos puntuales sin sobreescribir toda la entidad

        when(categoriaRepository.findById(1L)).thenReturn(java.util.Optional.of(existingCategoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(existingCategoria);// al guardar cualquier categoría parcheada
                                                                                 //el repositorio la retorna como si el cambio se hubiese persistido exitosamente


        Categoria patchedCategoria = categoriaService.patchCategoria(1L, patchData); // Llama al método real de service
        assertNotNull(patchedCategoria);// Verifica que la categoría no sea null
        assertEquals("Racing", patchedCategoria.getNombreCategoria()); // Verifica que el nombre de la categoría haya sido actualizado
    }

    @Test
    public void testDeleteById() { // Verifica que la eliminación de una categoría por su id se realice correctamente
        Categoria categoria = createCategoria();

        // Simula que al buscar por ID, encuentra la categoria
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        // Simula que al eliminar por ID, no pasa nada (osea, éxito)
        doNothing().when(categoriaRepository).delete(categoria);

        categoriaService.delete(1L); // Llama al método a probar

        // Aquí verifica que se llamó a findById y delete correctamente
        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).delete(categoria);
    }

}

