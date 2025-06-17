package com.valhallaride.valhallaride;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.valhallaride.valhallaride.model.Categoria;
import com.valhallaride.valhallaride.model.MetodoPago;
import com.valhallaride.valhallaride.model.Orden;
import com.valhallaride.valhallaride.model.Producto;
import com.valhallaride.valhallaride.model.ProductoOrden;
import com.valhallaride.valhallaride.model.Rol;
import com.valhallaride.valhallaride.model.Tienda;
import com.valhallaride.valhallaride.model.Usuario;
import com.valhallaride.valhallaride.repository.CategoriaRepository;
import com.valhallaride.valhallaride.repository.MetodoPagoRepository;
import com.valhallaride.valhallaride.repository.OrdenRepository;
import com.valhallaride.valhallaride.repository.ProductoOrdenRepository;
import com.valhallaride.valhallaride.repository.ProductoRepository;
import com.valhallaride.valhallaride.repository.RolRepository;
import com.valhallaride.valhallaride.repository.TiendaRepository;
import com.valhallaride.valhallaride.repository.UsuarioRepository;

import net.datafaker.Faker;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner{

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ProductoOrdenRepository productoOrdenRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

    // Lista de categorías de motos
    List<String> categoriasMotos = new ArrayList<>(List.of(
    "Deportiva", "Scooter", "Enduro",
    "Custom", "Touring", "Motocross", "Trial", "Chopper"
));

    Collections.shuffle(categoriasMotos); 

    for (int i = 0; i < categoriasMotos.size(); i++) {
        Categoria categoria = new Categoria();
        //categoria.setIdCategoria(i + 1); // Solo si NO es auto-generado
        categoria.setNombreCategoria(categoriasMotos.get(i));
        categoriaRepository.save(categoria);
    }

    List<Categoria> categorias = categoriaRepository.findAll();


    // Crear tiendas de motos
    for (int i = 0; i < 10; i++) {
        Tienda tienda = new Tienda();
        tienda.setNombreTienda(faker.company().name() + "Motos");
        tienda.setDireccionTienda(faker.address().fullAddress());
        tiendaRepository.save(tienda);
        }

    List<Tienda> tiendas = tiendaRepository.findAll();
    
    // Crear productos
    for(int i = 0; i < 5; i++){
        Producto producto = new Producto();
        producto.setNombreProducto(faker.vehicle().model()+ "Moto");
        producto.setDescripcionProducto(faker.lorem().sentence());
        producto.setPrecioProducto(random.nextInt(10000000) + 100000);
        producto.setStockProducto(random.nextInt(100) + 10);
        producto.setCategoria(categorias.get(random.nextInt(categorias.size())));
        producto.setTienda(tiendas.get(random.nextInt(tiendas.size())));
        productoRepository.save(producto);
    }

    List<Producto> productos = productoRepository.findAll();

    for(int i = 0; i < 3; i++){
        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setNomMetPago(faker.company().name());
        metodoPagoRepository.save(metodoPago);
    }
    List<MetodoPago> metodosPagos = metodoPagoRepository.findAll(); 
    
    List<String> tipoRoles = new ArrayList<>(List.of(
        "Admin", "Usuario"
    ));

    Collections.shuffle(tipoRoles);

    for(int i = 0; i < tipoRoles.size() ; i++){
        Rol rol = new Rol();
        rol.setNombreRol(tipoRoles.get(i));
        rolRepository.save(rol);
    }
    
    List<Rol> roles = rolRepository.findAll();

    for(int i = 0; i < 5; i++){
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(faker.name().fullName());
        usuario.setCorreoUsuario(faker.internet().emailAddress());
        usuario.setContrasena(faker.internet().password());
        usuario.setRol(roles.get(random.nextInt(roles.size())));
        usuarioRepository.save(usuario);
    }

    List<Usuario> usuarios = usuarioRepository.findAll();

    for(int i = 0; i < 5; i++){
        Orden orden = new Orden();
        orden.setFecha(LocalDate.now());
        orden.setPagado(faker.bool().bool());
        orden.setUsuario(usuarios.get(random.nextInt(usuarios.size())));
        orden.setMetodopago(metodosPagos.get(random.nextInt(metodosPagos.size())));
    }

    List<Orden> ordenes = ordenRepository.findAll();

    // probando probando
    for(int i = 0; i < 5; i++){
        ProductoOrden productoOrden = new ProductoOrden();
        productoOrden.setCantidad(faker.number().numberBetween(1, 10));
        
        Producto productoElegido = productos.get(random.nextInt(productos.size())); 
        productoOrden.setPrecioProducto(productoElegido.getPrecioProducto()); // Chicos, aqui accedemos al precio
        productoOrden.setProducto(productoElegido); // Y aqui le asignamos el producto

        productoOrden.setFechaHora(LocalDateTime.now());
        productoOrden.setOrden(ordenes.get(random.nextInt(ordenes.size())));

        productoOrdenRepository.save(productoOrden);
    }
    
    }
}
