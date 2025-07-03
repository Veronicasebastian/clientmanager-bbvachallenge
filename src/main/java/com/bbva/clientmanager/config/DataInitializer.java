package com.bbva.clientmanager.config;

import com.bbva.clientmanager.entity.ProductoBancario;
import com.bbva.clientmanager.entity.TipoProductoBancario;
import com.bbva.clientmanager.repository.IProductoBancarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Se inicializa la tabla de productos_bancarios en memoria
 * a fines del challenge
 * Con el contenido de la clase TipoProductoBancario
 *
 * @author Veronica
 */
@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(IProductoBancarioRepository productoBancarioRepository) {
        return args -> {
            for (TipoProductoBancario tipo : TipoProductoBancario.values()) {
                if (!productoBancarioRepository.existsByTipoProductoBancario(tipo)) {
                    ProductoBancario producto = new ProductoBancario();
                    producto.setTipoProductoBancario(tipo);
                    productoBancarioRepository.save(producto);
                }
            }
        };
    }
}
