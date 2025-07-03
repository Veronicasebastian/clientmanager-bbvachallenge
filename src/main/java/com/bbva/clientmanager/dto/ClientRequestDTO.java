package com.bbva.clientmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientRequestDTO {
    @NotNull(message = "El tipo de documento es obligatorio")
    private String TipoDocumento;
    @NotBlank(message = "El documento es obligatorio")
    @Size(min = 7, max = 15, message = "El documento debe tener entre 7 y 15 caracteres")
    private String documento;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    @NotBlank(message = "La calle es obligatoria")
    private String calle;
    @NotNull(message = "El número es obligatorio")
    private Integer numero;
    @NotBlank(message = "El código postal es obligatorio")
    private String codigoPostal;
    private String telefono;
    @NotBlank(message = "El celular es obligatorio")
    @Pattern(regexp = "\\d{6,15}", message = "El celular debe contener entre 6 y 15 dígitos")
    private String celular;
    private List<String> productoBancarioList;
}
