package edu.cibertec.appinventario.dto;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {}
