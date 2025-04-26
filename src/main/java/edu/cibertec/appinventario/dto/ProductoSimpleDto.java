package edu.cibertec.appinventario.dto;

import java.math.BigDecimal;

public record ProductoSimpleDto(
        Integer id,
        String codigo,
        String nombre,
        BigDecimal precio,
        Integer stock
) {}
