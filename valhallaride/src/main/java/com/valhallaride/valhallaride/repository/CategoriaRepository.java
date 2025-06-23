package com.valhallaride.valhallaride.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valhallaride.valhallaride.model.Categoria;
import java.util.List;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /*
    Optional<Categoria> findById(Integer idCategoria); - Este no se agrega, 
    ya que usando JpaRepository<Categoria, Integer> ya da todos los metodos basicos como:
    - findById(Integer id)
    - findAll()
    - save(...)
    - deleteById()
    */ 
    
    List <Categoria> findByNombreCategoria(String nombreCategoria); // Agreamos este metodo personalizado porque no viene por defecto como los de arriba


}

