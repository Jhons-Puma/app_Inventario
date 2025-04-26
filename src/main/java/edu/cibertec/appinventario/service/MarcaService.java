package edu.cibertec.appinventario.service;


import edu.cibertec.appinventario.dto.MarcaRequestDto;
import edu.cibertec.appinventario.dto.MarcaResponseDto;
import edu.cibertec.appinventario.dto.MarcaSimpleDto;
import edu.cibertec.appinventario.dto.PageResponseDto;

import java.util.List;

public interface MarcaService {

    // Operaciones CRUD básicas
    MarcaResponseDto create(MarcaRequestDto requestDto);
    MarcaResponseDto getById(Integer id);
    MarcaResponseDto update(Integer id, MarcaRequestDto requestDto);
    void delete(Integer id);

    // Operaciones adicionales
    List<MarcaResponseDto> getAll();
    PageResponseDto<MarcaResponseDto> getPaginated(int page, int size);
    List<MarcaSimpleDto> getAllSimple();
    List<MarcaResponseDto> findByNombre(String nombre);
    List<MarcaResponseDto> findByPaisOrigen(String paisOrigen);
    boolean existsByNombre(String nombre);
}
