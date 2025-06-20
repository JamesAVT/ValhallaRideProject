package com.valhallaride.valhallaride.model;

import jakarta.persistence.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tienda")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // chicos, usamos esto porque generaba un error en la serialización, ocultando así el objeto. 
public class Tienda {                                          // Con esta anotación, si encuentra el objeto

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTienda;

    @Column(length = 100, nullable = false, unique = true)
    private String nombreTienda;

    @Column(length = 100, nullable = false)
    private String direccionTienda;

    @OneToMany(mappedBy = "tienda")
    @JsonIgnore
    private List<Producto> productos;

}
