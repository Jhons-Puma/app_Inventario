package edu.cibertec.appinventario.dto;

import java.time.LocalDateTime;

public record CategoriaResponseDto(
        Integer id,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDateTime fechaCreacion
) {}
