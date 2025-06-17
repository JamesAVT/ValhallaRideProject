package com.valhallaride.valhallaride.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valhallaride.valhallaride.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long>  {

    Optional<Rol> findByNombreRol(String nombreRol);
    

}