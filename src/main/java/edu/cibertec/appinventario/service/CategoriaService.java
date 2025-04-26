package edu.cibertec.appinventario.service;

import edu.cibertec.appinventario.dto.CategoriaRequestDto;
import edu.cibertec.appinventario.dto.CategoriaResponseDto;
import edu.cibertec.appinventario.dto.CategoriaSimpleDto;
import edu.cibertec.appinventario.dto.PageResponseDto;

import java.util.List;

public interface CategoriaService {

    // Operaciones CRUD básicas
    CategoriaResponseDto create(CategoriaRequestDto requestDto);
    CategoriaResponseDto getById(Integer id);
    CategoriaResponseDto update(Integer id, CategoriaRequestDto requestDto);
    void delete(Integer id);

    // Operaciones adicionales
    List<CategoriaResponseDto> getAll();
    PageResponseDto<CategoriaResponseDto> getPaginated(int page, int size);
    List<CategoriaSimpleDto> getAllSimple();
    List<CategoriaResponseDto> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
