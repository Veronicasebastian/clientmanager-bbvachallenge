package com.bbva.clientmanager.exception;

import com.bbva.clientmanager.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Clase de configuración global para manejo de excepciones en la aplicación.
 * Define handlers específicos para distintas excepciones personalizadas y de Spring,
 * devolviendo respuestas estructuradas con {ExceptionDTO}.
 *
 * Se encarga de:
 *
 *   Manejo de cliente no encontrado.
 *   Errores en enums inválidos.
 *   Errores de validación de campos.
 *   Errores de tipo de parámetros en requests.
 *   Errores de formato de mensajes JSON.
 *
 *
 * @author Veronica
 */
@ControllerAdvice
public class ExceptionConfiguration {
    /**
     * Maneja la excepción cuando no se encuentra un cliente.
     *
     * @param e excepción lanzada
     * @return respuesta con mensaje y estado NOT_FOUND
     */
    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleClientNotFound(ClientNotFoundException e){
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage());
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja la excepción cuando se recibe un valor de enum inválido.
     *
     * @param e excepción lanzada
     * @return respuesta con mensaje y estado BAD_REQUEST
     */
    @ExceptionHandler(ValorEnumInvalidoException.class)
    public ResponseEntity<ExceptionDTO> handleValorEnumInvalido(ValorEnumInvalidoException e){
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage());
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja los errores de validación de campos en requests.
     *
     * @param ex excepción lanzada
     * @return respuesta con detalle del campo y mensaje de error, estado BAD_REQUEST
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message;
        if (fieldError != null) {
            message = "Error en el campo '" + fieldError.getField() + "': " + fieldError.getDefaultMessage();
        } else {
            message = "Error de validación.";
        }
        ExceptionDTO exceptionDTO = new ExceptionDTO(message);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores cuando un parámetro de request no tiene el tipo esperado.
     *
     * @param ex excepción lanzada
     * @return respuesta con mensaje indicando el parámetro inválido, estado BAD_REQUEST
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "El parámetro '" + ex.getName() + "' debe ser un número válido. Valor recibido: '" + ex.getValue() + "'";
        ExceptionDTO exceptionDTO = new ExceptionDTO(message);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de formato en el cuerpo de la request (JSON inválido).
     *
     * @param ex excepción lanzada
     * @return respuesta con mensaje de error de formato, estado BAD_REQUEST
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Error en el formato del JSON: " + ex.getMessage();
        ExceptionDTO dto = new ExceptionDTO(message);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
