package com.valhallaride.valhallaride.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orden")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOrden;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Boolean pagado;

    @ManyToOne(fetch = FetchType.LAZY)                  // Chicos, recuerden que fetch nos permite hacer consultas mas rapidas, porque evita traer toda 
    @JoinColumn(name = "idUsuario", nullable = false)  // la información relacionada automaticamente, y solo nos trae lo que necesitamos en el momento
    private Usuario usuario;

    @OneToMany(mappedBy = "orden", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductoOrden> productosOrden;

    @ManyToOne
    @JoinColumn(name = "id_met_pago", nullable = false)
    private MetodoPago metodopago;
}
