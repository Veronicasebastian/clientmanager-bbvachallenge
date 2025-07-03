package com.bbva.clientmanager.service;

import com.bbva.clientmanager.dto.ClientRequestDTO;
import com.bbva.clientmanager.dto.ClientRequestUpdateDTO;
import com.bbva.clientmanager.dto.ClientResponseDTO;
import com.bbva.clientmanager.dto.TelefonoUpdateDTO;

import java.util.List;

public interface IClientService {
    ClientResponseDTO create (ClientRequestDTO clientRequestDTO);

    List<ClientResponseDTO> findAll();

    ClientResponseDTO findById(Long id);

    List<ClientResponseDTO> findByProductoBancario(String tipoProductoBancario);

    void deleteById(Long id);

    ClientResponseDTO update(Long id, ClientRequestDTO clientRequestDTO);

    ClientResponseDTO partialUpdate(Long id, ClientRequestUpdateDTO dto);

    ClientResponseDTO updateTelefono(Long id, TelefonoUpdateDTO telefonoUpdateDTO);
}
