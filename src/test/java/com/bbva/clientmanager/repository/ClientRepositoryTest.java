package com.bbva.clientmanager.repository;

import com.bbva.clientmanager.entity.Client;
import com.bbva.clientmanager.entity.TipoDocumento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class ClientRepositoryTest {
    @Autowired
    private IClientRepository clientRepository;

    @Test
    public void testPersistenciaFechas() throws InterruptedException {
        Client client = new Client();
        client.setNombre("Veronica");
        client.setApellido("Sebastian");
        client.setDocumento("30000123");
        client.setTipoDocumento(TipoDocumento.DNI);
        client.setCalle("Calle Falsa");
        client.setNumero(1234);
        client.setCodigoPostal("C1416");
        client.setTelefono("45678788");
        client.setCelular("1557444444");

        Client savedClient = clientRepository.save(client);

        assertThat(savedClient.getFechaCreacion()).isNotNull();
        assertThat(savedClient.getFechaModificacion()).isNotNull();

        LocalDateTime fechaCreacionOriginal = savedClient.getFechaCreacion();
        LocalDateTime fechaModificacionOriginal = savedClient.getFechaModificacion();

        Thread.sleep(10);

        savedClient.setNombre("Veronica Modificada");
        Client updatedClient = clientRepository.save(savedClient);


        assertThat(updatedClient.getFechaModificacion()).isAfterOrEqualTo(fechaModificacionOriginal);
        assertThat(updatedClient.getFechaCreacion()).isEqualTo(fechaCreacionOriginal);
    }
}
