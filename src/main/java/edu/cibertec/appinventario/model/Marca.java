package edu.cibertec.appinventario.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "marcas")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "productos")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "pais_origen", length = 100)
    private String paisOrigen;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "marca")
    private List<Producto> productos = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
    }
}
