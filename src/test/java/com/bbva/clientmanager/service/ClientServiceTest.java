package com.bbva.clientmanager.service;

import com.bbva.clientmanager.dto.ClientRequestDTO;
import com.bbva.clientmanager.dto.ClientRequestUpdateDTO;
import com.bbva.clientmanager.dto.ClientResponseDTO;
import com.bbva.clientmanager.dto.TelefonoUpdateDTO;
import com.bbva.clientmanager.entity.Client;
import com.bbva.clientmanager.entity.ProductoBancario;
import com.bbva.clientmanager.entity.TipoDocumento;
import com.bbva.clientmanager.entity.TipoProductoBancario;
import com.bbva.clientmanager.exception.ClientNotFoundException;
import com.bbva.clientmanager.exception.ValorEnumInvalidoException;
import com.bbva.clientmanager.repository.IClientRepository;
import com.bbva.clientmanager.repository.IProductoBancarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private IClientRepository clientRepository;
    @Mock
    private IProductoBancarioRepository productoBancarioRepository;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private ClientService clientService;

    private Client client;


    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setNombre("Veronica");
        client.setDocumento("30000123");
        client.setTipoDocumento(TipoDocumento.DNI);
    }

    @Test
    void testCreate() {
        ClientRequestDTO request = new ClientRequestDTO();
        request.setDocumento("30000123");
        request.setNombre("Veronica");
        request.setApellido("Sebastian");
        request.setTipoDocumento("DNI");
        request.setCalle("Calle Falsa");
        request.setNumero(1234);
        request.setCodigoPostal("C1416");
        request.setTelefono("45678788");
        request.setCelular("1557444444");
        request.setProductoBancarioList(List.of("CHEQ"));

        ProductoBancario productoBancario = mock(ProductoBancario.class);
        when(productoBancario.getTipoProductoBancario()).thenReturn(TipoProductoBancario.CHEQ);

        client.setApellido("Sebastian");
        client.setCalle("Calle Falsa");
        client.setNumero(1234);
        client.setCodigoPostal("C1416");
        client.setTelefono("45678788");
        client.setCelular("1557444444");
        client.setProductoBancarioList(List.of(productoBancario));

        when(productoBancarioRepository.findByTipoProductoBancarioIn(anyList()))
                .thenReturn(List.of(productoBancario));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Act
        ClientResponseDTO result = clientService.create(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Veronica", result.getNombre());
        assertEquals("Sebastian", result.getApellido());
        assertEquals("30000123", result.getDocumento());
        assertEquals("DNI", result.getTipoDocumento());
        assertEquals("Calle Falsa", result.getCalle());
        assertEquals(1234, result.getNumero());
        assertEquals("C1416", result.getCodigoPostal());
        assertEquals("45678788", result.getTelefono());
        assertEquals("1557444444", result.getCelular());
        assertEquals(TipoProductoBancario.CHEQ.name(), result.getProductoBancarioList().get(0));

        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void testFindById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientResponseDTO result = clientService.findById(1L);

        assertNotNull(result);
        assertEquals("Veronica", result.getNombre());
        verify(clientRepository).findById(1L);
    }

    @Test
    void testFindByIdThrowsClientNotFoundException() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ClientNotFoundException ex = assertThrows(ClientNotFoundException.class, () -> clientService.findById(1L));
        assertTrue(ex.getMessage().contains("No se encuentra el cliente"));
    }

    @Test
    void testFindAll() {
        when(clientRepository.findAll()).thenReturn(List.of(client));

        List<ClientResponseDTO> result = clientService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testFindByProductoBancario() {
        ProductoBancario producto = new ProductoBancario();
        producto.setId(1L);
        producto.setTipoProductoBancario(TipoProductoBancario.CHEQ);
        client.setProductoBancarioList(List.of(producto));

        when(clientRepository.findByProductoBancarioList_TipoProductoBancario(TipoProductoBancario.CHEQ))
                .thenReturn(List.of(client));

        List<ClientResponseDTO> result = clientService.findByProductoBancario("CHEQ");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clientRepository).findByProductoBancarioList_TipoProductoBancario(TipoProductoBancario.CHEQ);
    }

    @Test
    void testFindByProductoBancarioThrowsValorEnumInvalidoException() {
        String invalidProducto = "INVALID_PRODUCTO";

        ValorEnumInvalidoException ex = assertThrows(ValorEnumInvalidoException.class, () -> {
            clientService.findByProductoBancario(invalidProducto);
        });

        assertTrue(ex.getMessage().contains("Tipo de producto bancario inválido"));
    }

    @Test
    void testDeleteById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.deleteById(1L);

        verify(clientRepository).deleteById(1L);
    }

    @Test
    void testDeleteByIdThrowsClientNotFoundException() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        ClientNotFoundException ex = assertThrows(ClientNotFoundException.class, () -> {
            clientService.deleteById(99L);
        });

        assertTrue(ex.getMessage().contains("No se encuentra el cliente"));
        verify(clientRepository).findById(99L);
    }

    @Test
    void testUpdate() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setDocumento("30000123");
        dto.setNombre("Veronica");
        dto.setApellido("Idola");
        dto.setTipoDocumento("DNI");

        ClientResponseDTO result = clientService.update(1L, dto);

        assertNotNull(result);
        assertEquals("Veronica", result.getNombre());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void testPartialUpdate() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientRequestUpdateDTO dto = new ClientRequestUpdateDTO();
        dto.setNombre("VeronicaUpdated");

        ClientResponseDTO result = clientService.partialUpdate(1L, dto);

        assertNotNull(result);
        assertEquals("VeronicaUpdated", result.getNombre());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void testUpdateTelefono() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        TelefonoUpdateDTO dto = new TelefonoUpdateDTO();
        dto.setTelefono("12345678");

        ClientResponseDTO result = clientService.updateTelefono(1L, dto);

        assertNotNull(result);
        assertEquals("12345678", result.getTelefono());
        verify(clientRepository).save(any(Client.class));
    }

}
