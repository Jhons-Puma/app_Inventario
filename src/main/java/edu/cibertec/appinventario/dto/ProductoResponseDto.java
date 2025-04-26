package edu.cibertec.appinventario.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoResponseDto(
        Integer id,
        String codigo,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        CategoriaSimpleDto categoria,
        MarcaSimpleDto marca,
        Boolean activo,
        LocalDateTime fechaCreacion
) {}
