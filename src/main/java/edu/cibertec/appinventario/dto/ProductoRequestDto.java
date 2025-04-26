package edu.cibertec.appinventario.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductoRequestDto(
        @NotBlank(message = "El código es obligatorio")
        @Size(min = 3, max = 50, message = "El código debe tener entre 3 y 50 caracteres")
        String codigo,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
        BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        @NotNull(message = "La categoría es obligatoria")
        Integer categoriaId,

        @NotNull(message = "La marca es obligatoria")
        Integer marcaId
) {}
