package com.valhallaride.valhallaride.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(length = 30, nullable = false)
    private String nombreUsuario;

    @Column(unique = true, length = 50, nullable = false)
    @JsonIgnore
    private String correoUsuario;

    @Column(length = 30, nullable = false)
    @JsonIgnore
    String contrasena;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Orden> ordenes;

    @ManyToOne
    @JoinColumn(name = "idRol", nullable = false)
    private Rol rol;
}
