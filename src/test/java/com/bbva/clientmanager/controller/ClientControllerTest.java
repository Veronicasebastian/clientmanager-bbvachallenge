package com.bbva.clientmanager.controller;

import com.bbva.clientmanager.dto.ClientRequestDTO;
import com.bbva.clientmanager.dto.ClientRequestUpdateDTO;
import com.bbva.clientmanager.dto.ClientResponseDTO;
import com.bbva.clientmanager.dto.TelefonoUpdateDTO;
import com.bbva.clientmanager.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {
    @Mock
    private ClientService clientService;

    private ClientController clientController;

    @BeforeEach
    void setUp() {
        clientController = new ClientController(clientService);
    }

    @Test
    public void testCreate(){
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setDocumento("30000123");
        clientRequestDTO.setNombre("Veronica");
        clientRequestDTO.setApellido("Sebastian");
        clientRequestDTO.setTipoDocumento("DNI");
        clientRequestDTO.setCalle("Calle Falsa");
        clientRequestDTO.setNumero(1234);
        clientRequestDTO.setCodigoPostal("C1416");
        clientRequestDTO.setTelefono("45678788");
        clientRequestDTO.setCelular("1557444444");
        clientRequestDTO.setProductoBancarioList(List.of("CHEQ"));

        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setId(1L);
        clientResponseDTO.setDocumento("30000123");
        clientResponseDTO.setNombre("Veronica");
        clientResponseDTO.setApellido("Sebastian");
        clientResponseDTO.setTipoDocumento("DNI");
        clientResponseDTO.setCalle("Calle Falsa");
        clientResponseDTO.setNumero(1234);
        clientResponseDTO.setCodigoPostal("C1416");
        clientResponseDTO.setTelefono("45678788");
        clientResponseDTO.setCelular("1557444444");
        clientResponseDTO.setProductoBancarioList(List.of("CHEQ"));

        when(clientService.create(clientRequestDTO)).thenReturn(clientResponseDTO);

        ResponseEntity<ClientResponseDTO> response = clientController.create(clientRequestDTO);
        assertEquals("Veronica", response.getBody().getNombre());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    public void testFindAll(){
        ClientResponseDTO dto1 = new ClientResponseDTO();
        dto1.setId(1L);
        dto1.setNombre("Cliente1");

        ClientResponseDTO dto2 = new ClientResponseDTO();
        dto2.setId(2L);
        dto2.setNombre("Cliente2");

        when(clientService.findAll()).thenReturn(List.of(dto1, dto2));

        ResponseEntity<List<ClientResponseDTO>> response = clientController.getAll();

        assertEquals(2, response.getBody().size());
        assertEquals("Cliente1", response.getBody().get(0).getNombre());
    }

    @Test
    public void testFindById(){
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setId(1L);
        dto.setNombre("Cliente1");

        when(clientService.findById(1L)).thenReturn(dto);

        ResponseEntity<ClientResponseDTO> response = clientController.findById(1L);

        assertEquals("Cliente1", response.getBody().getNombre());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    public void testDeleteById(){
        doNothing().when(clientService).deleteById(1L);

        ResponseEntity<Void> response = clientController.deleteById(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(clientService, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdate(){
        ClientRequestDTO request = new ClientRequestDTO();
        request.setNombre("Actualizado");

        ClientResponseDTO responseDto = new ClientResponseDTO();
        responseDto.setId(1L);
        responseDto.setNombre("Actualizado");

        when(clientService.update(1L, request)).thenReturn(responseDto);

        ResponseEntity<ClientResponseDTO> response = clientController.update(1L, request);

        assertEquals("Actualizado", response.getBody().getNombre());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    public void testPartialUpdate(){
        ClientRequestUpdateDTO updateDTO = new ClientRequestUpdateDTO();
        updateDTO.setNombre("Parcial");

        ClientResponseDTO responseDto = new ClientResponseDTO();
        responseDto.setId(1L);
        responseDto.setNombre("Parcial");

        when(clientService.partialUpdate(1L, updateDTO)).thenReturn(responseDto);

        ResponseEntity<ClientResponseDTO> response = clientController.partialUpdate(1L, updateDTO);

        assertEquals("Parcial", response.getBody().getNombre());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    public void testUpdateTelefono(){
        TelefonoUpdateDTO telDTO = new TelefonoUpdateDTO();
        telDTO.setTelefono("12345678");

        ClientResponseDTO responseDto = new ClientResponseDTO();
        responseDto.setId(1L);
        responseDto.setTelefono("12345678");

        when(clientService.updateTelefono(1L, telDTO)).thenReturn(responseDto);

        ResponseEntity<ClientResponseDTO> response = clientController.updateTelefono(1L, telDTO);

        assertEquals("12345678", response.getBody().getTelefono());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    public void testGetClientsByProductoBancario(){
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setId(1L);
        dto.setNombre("ClienteProducto");

        when(clientService.findByProductoBancario("CHEQ")).thenReturn(List.of(dto));

        ResponseEntity<List<ClientResponseDTO>> response = clientController.getClientsByProductoBancario("CHEQ");

        assertEquals(1, response.getBody().size());
        assertEquals("ClienteProducto", response.getBody().get(0).getNombre());
    }
}
