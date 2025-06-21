package com.valhallaride.valhallaride.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valhallaride.valhallaride.model.MetodoPago;
import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.repository.MetodoPagoRepository;
import com.valhallaride.valhallaride.repository.OrdenRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    public List<MetodoPago> findAll() {
        return metodoPagoRepository.findAll();
    }

    public MetodoPago findById(Long id) {
        return metodoPagoRepository.findById(id).orElse(null);
    }

    public MetodoPago save(MetodoPago metodoPago) {
        return metodoPagoRepository.save(metodoPago);
    }

    public void delete(long id) {
        // Aqui verificamos si el metodo pago existe
        MetodoPago metodoPago = metodoPagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Metodo de pago no encontrado"));

        // Aca, hacemos un listado de ordenes que esten asociados al metodo pago
        List<Orden> ordenes = ordenRepository.findByMetodoPago(metodoPago);

        // Chicos, aqui se elimina las ordenes relacionadas con el metodo de pago!
        for (Orden orden : ordenes) {
            ordenRepository.delete(orden);
        }

        // Y aqui elimina el metodo de pago
        metodoPagoRepository.delete(metodoPago);
    }

    public MetodoPago patchMetodoPago(Long id, MetodoPago parcialMetodoPago) {
        Optional<MetodoPago> metodoPagOptional = metodoPagoRepository.findById(id);
        if (metodoPagOptional.isPresent()) {

            MetodoPago metodoPagoUpdate = metodoPagOptional.get();

            if (parcialMetodoPago.getNomMetPago() != null) {
                metodoPagoUpdate.setNomMetPago(parcialMetodoPago.getNomMetPago());
            }

            return metodoPagoRepository.save(metodoPagoUpdate);
        } else {
            return null;
        }
    }
}
