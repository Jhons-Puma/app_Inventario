package edu.cibertec.appinventario.service.impl;

import edu.cibertec.appinventario.dto.CategoriaRequestDto;
import edu.cibertec.appinventario.dto.CategoriaResponseDto;
import edu.cibertec.appinventario.dto.CategoriaSimpleDto;
import edu.cibertec.appinventario.dto.PageResponseDto;
import edu.cibertec.appinventario.exception.BadRequestException;
import edu.cibertec.appinventario.exception.ResourceNotFoundException;
import edu.cibertec.appinventario.mapper.CategoriaMapper;
import edu.cibertec.appinventario.model.Categoria;
import edu.cibertec.appinventario.repository.CategoriaRepository;
import edu.cibertec.appinventario.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    public CategoriaResponseDto create(CategoriaRequestDto requestDto) {
        log.info("Creando nueva categoría con nombre: {}", requestDto.nombre());

        // Verificar si ya existe una categoría con el mismo nombre
        if (categoriaRepository.existsByNombre(requestDto.nombre())) {
            throw new BadRequestException("Ya existe una categoría con el nombre: " + requestDto.nombre());
        }

        // Convertir DTO a entidad, guardar y convertir resultado a DTO de respuesta
        Categoria categoria = categoriaMapper.toEntity(requestDto);
        Categoria savedCategoria = categoriaRepository.save(categoria);

        log.info("Categoría creada exitosamente con ID: {}", savedCategoria.getId());
        return categoriaMapper.toDto(savedCategoria);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDto getById(Integer id) {
        log.info("Buscando categoría con ID: {}", id);

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));

        return categoriaMapper.toDto(categoria);
    }

    @Override
    public CategoriaResponseDto update(Integer id, CategoriaRequestDto requestDto) {
        log.info("Actualizando categoría con ID: {}", id);

        // Verificar si existe la categoría
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));

        // Verificar si existe otra categoría con el mismo nombre (que no sea la actual)
        if (!categoria.getNombre().equals(requestDto.nombre()) &&
                categoriaRepository.existsByNombre(requestDto.nombre())) {
            throw new BadRequestException("Ya existe otra categoría con el nombre: " + requestDto.nombre());
        }

        // Actualizar la entidad y guardar
        categoriaMapper.updateEntityFromDto(requestDto, categoria);
        Categoria updatedCategoria = categoriaRepository.save(categoria);

        log.info("Categoría actualizada exitosamente: {}", updatedCategoria.getId());
        return categoriaMapper.toDto(updatedCategoria);
    }

    @Override
    public void delete(Integer id) {
        log.info("Eliminando categoría con ID: {}", id);

        // Verificar si existe la categoría
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));

        // Desactivar la categoría en lugar de eliminarla físicamente
        categoria.setActivo(false);
        categoriaRepository.save(categoria);

        log.info("Categoría desactivada exitosamente: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> getAll() {
        log.info("Obteniendo todas las categorías");

        List<Categoria> categorias = categoriaRepository.findAll(Sort.by("nombre"));
        return categoriaMapper.toDtoList(categorias);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<CategoriaResponseDto> getPaginated(int page, int size) {
        log.info("Obteniendo categorías paginadas: página {}, tamaño {}", page, size);

        Page<Categoria> categoriasPage = categoriaRepository.findAll(
                PageRequest.of(page, size, Sort.by("nombre")));

        List<CategoriaResponseDto> content = categoriaMapper.toDtoList(categoriasPage.getContent());

        return new PageResponseDto<>(
                content,
                categoriasPage.getNumber(),
                categoriasPage.getSize(),
                categoriasPage.getTotalElements(),
                categoriasPage.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaSimpleDto> getAllSimple() {
        log.info("Obteniendo lista simple de todas las categorías");

        List<Categoria> categorias = categoriaRepository.findByActivoTrue();
        return categoriaMapper.toSimpleDtoList(categorias);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> findByNombre(String nombre) {
        log.info("Buscando categorías por nombre: {}", nombre);

        List<Categoria> categorias = categoriaRepository.findByNombreContainingIgnoreCase(nombre);
        return categoriaMapper.toDtoList(categorias);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        log.info("Verificando si existe categoría con nombre: {}", nombre);
        return categoriaRepository.existsByNombre(nombre);
    }
}
