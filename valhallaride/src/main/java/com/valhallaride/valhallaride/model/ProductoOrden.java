package com.valhallaride.valhallaride.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "producto_orden")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProductoOrden;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Integer precioProducto; // Precio en el momento de la compra

    @Column(nullable = false)
    private LocalDateTime fechaHora; // Fecha y hora del registro

    @ManyToOne
    @JoinColumn(name = "id_orden", nullable = false)
    @JsonBackReference
    private Orden orden;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
}
