package com.valhallaride.valhallaride.service;

import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.model.ProductoOrden;
import com.valhallaride.valhallaride.repository.OrdenRepository;
import com.valhallaride.valhallaride.repository.ProductoOrdenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private ProductoOrdenRepository productoOrdenRepository;

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

    // Query 2 - 3 tablas
    public List<Map<String, Object>> listarOrdenesDetalladas() {
    List<Object[]> resultados = ordenRepository.listarOrdenesConUsuarioYMetodoPago();
    List<Map<String, Object>> lista = new ArrayList<>();

    for (Object[] fila : resultados) {
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("id_orden", fila[0]);
        datos.put("fecha", fila[1]);
        datos.put("nombre_usuario", fila[2]);
        datos.put("metodo_pago", fila[3]);
        lista.add(datos);
    }

    return lista;
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
            ordenExistente.setMetodoPago(ordenActualizada.getMetodoPago());

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
        // Aca verificamos si la orden existe
        Orden orden = ordenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Aqui hacemos un listado de ProductoOrden asociados a la orden
        List<ProductoOrden> productoOrdenes = productoOrdenRepository.findByOrden(orden);

        // Aqui chicos elimina los ProductoOrden que esten asociados a una orden
        for (ProductoOrden productoOrden : productoOrdenes) {
            productoOrdenRepository.delete(productoOrden);
        }

        // Y aqui elimina la Orden!
        ordenRepository.delete(orden);
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
    
    public List<Orden> findByUsuarioMetodoPagoEstado(Long idUsuario, Long idMetPago, Boolean pagado) {
        return ordenRepository.findByUsuario_IdUsuarioAndMetodoPago_IdMetPagoAndPagado(idUsuario, idMetPago, pagado);
    }
}
