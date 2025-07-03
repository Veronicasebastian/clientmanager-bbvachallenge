package com.bbva.clientmanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ClientResponseDTO {
    private Long id;
    private String tipoDocumento;
    private String documento;
    private String nombre;
    private String apellido;
    private String calle;
    private Integer numero;
    private String codigoPostal;
    private String telefono;
    private String celular;
    private List<String> productoBancarioList;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}
