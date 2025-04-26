package edu.cibertec.appinventario.dto;

import java.time.LocalDateTime;

public record MarcaResponseDto(
        Integer id,
        String nombre,
        String descripcion,
        String paisOrigen,
        Boolean activo,
        LocalDateTime fechaCreacion
) {}
