package com.bbva.clientmanager.controller;

import com.bbva.clientmanager.dto.ClientRequestDTO;
import com.bbva.clientmanager.dto.ClientRequestUpdateDTO;
import com.bbva.clientmanager.dto.ClientResponseDTO;
import com.bbva.clientmanager.dto.TelefonoUpdateDTO;
import com.bbva.clientmanager.service.IClientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
/**
 * Controlador REST para gestionar los clientes de la aplicación.
 * Provee endpoints para crear, buscar, actualizar y eliminar clientes,
 * así como para búsquedas por producto bancario, actualizaciones parciales y
 * actualizacion de telefono a pedido del challenge.
 *
 * URL base: /clients
 *
 * @author Veronica
 */
@Slf4j
@RestController
@RequestMapping("/clients")
public class ClientController {
    private final IClientService clientService;

    public ClientController(IClientService clientService){
        this.clientService = clientService;
    }

    /**
     * Endpoint para crear un nuevo cliente.
     *
     * @param clientRequestDTO objeto con los datos del cliente a crear
     * @return ResponseEntity con el cliente creado y código HTTP 201 Created
     */
    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientRequestDTO clientRequestDTO){
        log.info("POST /clients - Creando cliente con documento: {}", clientRequestDTO.getDocumento());
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(clientRequestDTO));
    }

    /**
     * Endpoint para recuperar todos los clientes.
     *
     * @return ResponseEntity con la lista de todos los clientes y código HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAll(){
        log.info("GET /clients - Recuperando todos los clientes");
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findAll());
    }

    /**
     * Endpoint para buscar un cliente por su ID.
     *
     * @param id identificador del cliente
     * @return ResponseEntity con el cliente encontrado y código HTTP 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable Long id){
        log.info("GET /clients/{} - Buscando cliente por id", id);
        return ResponseEntity.status(HttpStatus.OK).body(clientService.findById(id));
    }

    /**
     * Endpoint para buscar clientes por tipo de producto bancario.
     *
     * @param tipoProductoBancario tipo de producto bancario (ej. "CHEQ")
     * @return ResponseEntity con la lista de clientes que poseen dicho producto
     */
    @GetMapping("/producto/{tipoProductoBancario}")
    public ResponseEntity<List<ClientResponseDTO>> getClientsByProductoBancario(
            @PathVariable String tipoProductoBancario){
        log.info("GET /clients/producto/{} - Buscando clientes por producto bancario", tipoProductoBancario);
        return ResponseEntity.ok(clientService.findByProductoBancario(tipoProductoBancario));
    }

    /**
     * Endpoint para eliminar un cliente por su ID.
     *
     * @param id identificador del cliente a eliminar
     * @return ResponseEntity sin contenido y código HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        log.info("DELETE /clients/{} - Eliminando cliente", id);
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para actualizar completamente los datos de un cliente.
     *
     * @param id identificador del cliente a actualizar
     * @param clientRequestDTO objeto con los nuevos datos del cliente
     * @return ResponseEntity con el cliente actualizado y código HTTP 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody ClientRequestDTO clientRequestDTO){
        log.info("PUT /clients/{} - Actualizando cliente", id);
        return ResponseEntity.status(HttpStatus.OK).body(clientService.update(id, clientRequestDTO));
    }

    /**
     * Endpoint para actualizar parcialmente los datos de un cliente.
     *
     * @param id identificador del cliente a actualizar
     * @param clientRequestUpdateDTO objeto con los campos a actualizar
     * @return ResponseEntity con el cliente actualizado parcialmente y código HTTP 200 OK
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> partialUpdate(
            @PathVariable Long id,
            @RequestBody ClientRequestUpdateDTO clientRequestUpdateDTO) {
        log.info("PATCH /clients/{} - Actualización parcial de cliente", id);
        return ResponseEntity.status(HttpStatus.OK).body(clientService.partialUpdate(id, clientRequestUpdateDTO));
    }

    /**
     * Endpoint para actualizar el teléfono de un cliente.
     *
     * @param id identificador del cliente
     * @param telefonoUpdateDTO objeto con el nuevo número de teléfono
     * @return ResponseEntity con el cliente actualizado y código HTTP 200 OK
     */
    @PatchMapping("/{id}/telefono")
    public ResponseEntity<ClientResponseDTO> updateTelefono(
            @PathVariable Long id,
            @RequestBody TelefonoUpdateDTO telefonoUpdateDTO){
        log.info("PATCH /clients/{}/telefono - Actualizando teléfono", id);
        return ResponseEntity.ok(clientService.updateTelefono(id, telefonoUpdateDTO));
    }


}
