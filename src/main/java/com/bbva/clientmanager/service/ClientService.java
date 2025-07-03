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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Implementación del servicio de clientes
 * Contiene la lógica de negocio para crear, buscar, actualizar y eliminar clientes,
 * así como validaciones de tipo de documento y productos bancarios.
 *
 * Usa {IClientRepository} para acceder a los clientes y {IProductoBancarioRepository}
 * para validar los productos bancarios.
 *
 * @author Veronica
 */
@Slf4j
@Service
public class ClientService implements IClientService{

    private final IClientRepository clientRepository;
    private final IProductoBancarioRepository productoBancarioRepository;
    private final ObjectMapper objectMapper;

    public ClientService(IClientRepository clientRepository, IProductoBancarioRepository productoBancarioRepository,
                         ObjectMapper objectMapper){
        this.clientRepository = clientRepository;
        this.objectMapper = objectMapper;
        this.productoBancarioRepository = productoBancarioRepository;
    }

    private static final String MESSAGE_CLIENT = "No se encuentra el cliente con id ";
    private static final String MESSAGE_TIPO_PRODUCTO = "Tipo de producto bancario inválido: ";
    private static final String MESSAGE_PRODUCTO = "Ninguno de los productos bancarios indicados existe en la base.";
    private static final String MESSAGE_TIPO_DOC = "Tipo de documento inválido: ";

    /**
     * Crea un nuevo cliente en la base de datos.
     *
     * @param clientRequestDTO datos del cliente a crear
     * @return cliente creado como ClientResponseDTO
     */
    @Override
    public ClientResponseDTO create(ClientRequestDTO clientRequestDTO) {
        log.info("Creando cliente con documento: {}", clientRequestDTO.getDocumento());
        Client client = mapToEntity(clientRequestDTO);
        return mapToDTO(clientRepository.save(client));
    }

    /**
     * Recupera todos los clientes de la base de datos.
     *
     * @return lista de clientes como ClientResponseDTO
     */
    @Override
    public List<ClientResponseDTO> findAll() {
        log.info("Recuperando todos los clientes");
        List<ClientResponseDTO> clientResponseDTOS =  clientRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
        log.info("Cantidad de clientes recuperados: {}", clientResponseDTOS.size());
        return clientResponseDTOS;
    }

    /**
     * Busca un cliente por su ID.
     *
     * @param id identificador del cliente
     * @return cliente encontrado como ClientResponseDTO
     * @throws ClientNotFoundException si no se encuentra el cliente
     */
    @Override
    public ClientResponseDTO findById(Long id) {
        log.info("Buscando cliente con id: {}", id);
        Client client = clientRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("No se encontró cliente con id: {}", id);
                    return new ClientNotFoundException(MESSAGE_CLIENT + id);
                });
        return mapToDTO(client);

    }

    /**
     * Busca clientes que tengan un producto bancario específico.
     *
     * @param tipoProductoBancario nombre del producto bancario
     * @return lista de clientes con dicho producto
     * @throws ValorEnumInvalidoException si el producto bancario no es válido
     */
    @Override
    public List<ClientResponseDTO> findByProductoBancario(String tipoProductoBancario){
        log.info("Buscando clientes con producto bancario: {}", tipoProductoBancario);
        TipoProductoBancario tipo;
        try {
            tipo = TipoProductoBancario.valueOf(tipoProductoBancario);
        } catch (IllegalArgumentException e){
            log.error("Tipo de producto bancario inválido: {}", tipoProductoBancario);
            throw new ValorEnumInvalidoException(MESSAGE_TIPO_PRODUCTO + tipoProductoBancario);
        }

        List<Client> clients = clientRepository.findByProductoBancarioList_TipoProductoBancario(tipo);
        log.info("Se encontraron {} clientes con producto bancario: {}", clients.size(), tipoProductoBancario);
        return clients.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Elimina un cliente por su ID.
     *
     * @param id identificador del cliente
     * @throws ClientNotFoundException si no se encuentra el cliente
     */
    @Override
    public void deleteById(Long id) {
        log.info("Eliminando cliente con id: {}", id);
        findById(id);
        clientRepository.deleteById(id);
        log.info("Cliente con id {} eliminado", id);
    }

    /**
     * Actualiza completamente un cliente.
     *
     * @param id identificador del cliente
     * @param clientRequestDTO nuevos datos del cliente
     * @return cliente actualizado como ClientResponseDTO
     */
    @Override
    public ClientResponseDTO update(Long id, ClientRequestDTO clientRequestDTO) {
        log.info("Actualizando cliente con id: {}", id);
        Client clientB = clientRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("No se encontró cliente con id: {}", id);
                    return new ClientNotFoundException(MESSAGE_CLIENT + id);
                });
        Client client = mapToEntity(clientRequestDTO);
        client.setId(id);
        client.setFechaCreacion(clientB.getFechaCreacion());
        ClientResponseDTO clientResponseDTO = mapToDTO(clientRepository.save(client));
        log.info("Cliente con id {} actualizado", id);
        return clientResponseDTO;
    }

    /**
     * Realiza una actualización parcial de un cliente.
     *
     * @param id identificador del cliente
     * @param dto datos parciales a actualizar
     * @return cliente actualizado como ClientResponseDTO
     * @throws ClientNotFoundException si no se encuentra el cliente
     */
    @Override
    public ClientResponseDTO partialUpdate(Long id, ClientRequestUpdateDTO dto) {
        log.info("Actualización parcial de cliente con id: {}", id);
        Client client = clientRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("No se encontró cliente con id: {} para actualización parcial", id);
                    return new ClientNotFoundException(MESSAGE_CLIENT + id);
                } );

        if (dto.getNombre() != null) client.setNombre(dto.getNombre());
        if (dto.getApellido() != null) client.setApellido(dto.getApellido());
        if (dto.getCalle() != null) client.setCalle(dto.getCalle());
        if (dto.getNumero() != null) client.setNumero(dto.getNumero());
        if (dto.getCodigoPostal() != null) client.setCodigoPostal(dto.getCodigoPostal());
        if (dto.getTelefono() != null) client.setTelefono(dto.getTelefono());
        if (dto.getCelular() != null) client.setCelular(dto.getCelular());

        if (dto.getTipoDocumento() != null) {
            client.setTipoDocumento(validarTipoDocumento(dto.getTipoDocumento()));
        }

        if (dto.getProductoBancarioList() != null) {
            client.setProductoBancarioList(validarProductosBancarios(dto.getProductoBancarioList()));
        }

        ClientResponseDTO updatedDto = mapToDTO(clientRepository.save(client));
        log.info("Actualización parcial completada para cliente id: {}", id);

        return updatedDto;
    }

    /**
     * Actualiza solo el teléfono de un cliente.
     *
     * @param id identificador del cliente
     * @param telefonoUpdateDTO nuevo teléfono a actualizar
     * @return cliente actualizado como ClientResponseDTO
     * @throws ClientNotFoundException si no se encuentra el cliente
     */
    @Override
    public ClientResponseDTO updateTelefono(Long id, TelefonoUpdateDTO telefonoUpdateDTO){
        log.info("Actualizando teléfono del cliente con id: {}", id);
        Client client = clientRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("No se encontró cliente con id: {} para actualizar teléfono", id);
                    return new ClientNotFoundException(MESSAGE_CLIENT + id);
                });
        client.setTelefono(telefonoUpdateDTO.getTelefono());
        ClientResponseDTO clientResponseDTO = mapToDTO(clientRepository.save(client));
        log.info("Teléfono actualizado para cliente id: {}", id);
        return clientResponseDTO;
    }

    private ClientResponseDTO mapToDTO(Client client){
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();

        clientResponseDTO.setId(client.getId());
        clientResponseDTO.setTipoDocumento(client.getTipoDocumento().toString());
        clientResponseDTO.setDocumento(client.getDocumento());
        clientResponseDTO.setNombre(client.getNombre());
        clientResponseDTO.setApellido(client.getApellido());
        clientResponseDTO.setCalle(client.getCalle());
        clientResponseDTO.setNumero(client.getNumero());
        clientResponseDTO.setCodigoPostal(client.getCodigoPostal());
        clientResponseDTO.setTelefono(client.getTelefono());
        clientResponseDTO.setCelular(client.getCelular());
        clientResponseDTO.setFechaCreacion(client.getFechaCreacion());
        clientResponseDTO.setFechaModificacion(client.getFechaModificacion());

        List<String> productos = client.getProductoBancarioList() != null ? client.getProductoBancarioList().stream()
                .map(pb -> pb.getTipoProductoBancario().name())
                .collect(Collectors.toList()) : null;

        clientResponseDTO.setProductoBancarioList(productos);

        return clientResponseDTO;
    }

    private Client mapToEntity (ClientRequestDTO clientRequestDTO){
        Client client = new Client();
        client.setDocumento(clientRequestDTO.getDocumento());
        client.setNombre(clientRequestDTO.getNombre());
        client.setApellido(clientRequestDTO.getApellido());
        client.setCalle(clientRequestDTO.getCalle());
        client.setNumero(clientRequestDTO.getNumero());
        client.setCodigoPostal(clientRequestDTO.getCodigoPostal());
        client.setTelefono(clientRequestDTO.getTelefono());
        client.setCelular(clientRequestDTO.getCelular());

        client.setProductoBancarioList(validarProductosBancarios(clientRequestDTO.getProductoBancarioList()));
        client.setTipoDocumento(validarTipoDocumento(clientRequestDTO.getTipoDocumento()));

        return client;
    }

    private List<ProductoBancario> validarProductosBancarios(List<String> nombres) {
        if (nombres == null) return null;
        List<TipoProductoBancario> tipoProductoBancarios = nombres.stream()
                .map(nombre -> {
                    try {
                        return TipoProductoBancario.valueOf(nombre);
                    } catch (IllegalArgumentException e) {
                        log.error(MESSAGE_TIPO_PRODUCTO + nombre);
                        throw new ValorEnumInvalidoException(MESSAGE_TIPO_PRODUCTO + nombre);
                    }
                })
                .collect(Collectors.toList());

        List<ProductoBancario> productos = productoBancarioRepository.findByTipoProductoBancarioIn(tipoProductoBancarios);

        if (productos.isEmpty() && !tipoProductoBancarios.isEmpty()) {
            log.error(MESSAGE_PRODUCTO);
            throw new ValorEnumInvalidoException(MESSAGE_PRODUCTO);
        }
        return productos;
    }

    private TipoDocumento validarTipoDocumento(String tipoDocumento) {
        try {
            return TipoDocumento.valueOf(tipoDocumento);
        } catch (IllegalArgumentException e) {
            log.error(MESSAGE_TIPO_DOC + tipoDocumento);
            throw new ValorEnumInvalidoException(MESSAGE_TIPO_DOC + tipoDocumento);
        }
    }
}
