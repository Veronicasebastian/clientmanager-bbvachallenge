package com.bbva.clientmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoDocumento TipoDocumento;
    private String documento;
    private String nombre;
    private String apellido;
    private String calle;
    private Integer numero;
    private String codigoPostal;
    private String telefono;
    private String celular;
    @ManyToMany
    @JoinTable(
            name = "clients_productos_bancarios",
            joinColumns = @JoinColumn(name = "clients_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_bancario_id")
    )
    private List<ProductoBancario> productoBancarioList;
    @Column(name = "fecha_creacion", updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    @UpdateTimestamp
    private LocalDateTime fechaModificacion;
}
