package com.valhallaride.valhallaride.service;

import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    public List<Orden> findAll() {
        return ordenRepository.findAll();
    }

    public Orden findById(Integer id) {
        return ordenRepository.findById(id).orElse(null);
    }

    public List<Orden> findByUsuario(Integer idUsuario) {
        return ordenRepository.findByUsuario_IdUsuario(idUsuario);
    }

    public List<Orden> findByPagado(Boolean pagado) {
        return ordenRepository.findByPagado(pagado);
    }

    public Orden save(Orden orden) {
        if (orden.getFecha() == null) {
            orden.setFecha(LocalDate.now());
        }
        if (orden.getPagado() == null) {
            orden.setPagado(false);
        }
        return ordenRepository.save(orden);
    }

    public Orden update(Integer id, Orden ordenActualizada) {
        Optional<Orden> optionalOrden = ordenRepository.findById(id);
        if (optionalOrden.isPresent()) {
            Orden ordenExistente = optionalOrden.get();
            ordenExistente.setFecha(ordenActualizada.getFecha());
            ordenExistente.setPagado(ordenActualizada.getPagado());
            ordenExistente.setUsuario(ordenActualizada.getUsuario());
            ordenExistente.setMetodopago(ordenActualizada.getMetodopago());

            return ordenRepository.save(ordenExistente);
        }
        return null;
    }

    public Orden actualizarEstadoPago(Integer id, Boolean pagado) {
        Orden orden = ordenRepository.findById(id).orElse(null);
        if (orden != null) {
            orden.setPagado(pagado);
            return ordenRepository.save(orden);
        }
        return null;
    }

    public void delete(Integer id) {
        ordenRepository.deleteById(id);
    }

    public Orden patchOrden(Integer id, Orden parcialOrden) {
        Optional<Orden> ordenOptional = ordenRepository.findById(id);
        if (ordenOptional.isPresent()){

            Orden ordenToUpdate = ordenOptional.get();

            if (parcialOrden.getFecha() != null){
                ordenToUpdate.setFecha(parcialOrden.getFecha());
            }

            if (parcialOrden.getPagado() != null){
                ordenToUpdate.setPagado(parcialOrden.getPagado());
            }

            return ordenRepository.save(ordenToUpdate);
        } else{
            return null;
        }
    }
}
