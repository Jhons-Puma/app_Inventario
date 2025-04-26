package edu.cibertec.appinventario.service.impl;

import edu.cibertec.appinventario.dto.MarcaRequestDto;
import edu.cibertec.appinventario.dto.MarcaResponseDto;
import edu.cibertec.appinventario.dto.MarcaSimpleDto;
import edu.cibertec.appinventario.dto.PageResponseDto;
import edu.cibertec.appinventario.exception.BadRequestException;
import edu.cibertec.appinventario.exception.ResourceNotFoundException;
import edu.cibertec.appinventario.mapper.MarcaMapper;
import edu.cibertec.appinventario.model.Marca;
import edu.cibertec.appinventario.repository.MarcaRepository;
import edu.cibertec.appinventario.service.MarcaService;
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
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    @Override
    public MarcaResponseDto create(MarcaRequestDto requestDto) {
        log.info("Creando nueva marca con nombre: {}", requestDto.nombre());

        // Verificar si ya existe una marca con el mismo nombre
        if (marcaRepository.existsByNombre(requestDto.nombre())) {
            throw new BadRequestException("Ya existe una marca con el nombre: " + requestDto.nombre());
        }

        // Convertir DTO a entidad, guardar y convertir resultado a DTO de respuesta
        Marca marca = marcaMapper.toEntity(requestDto);
        Marca savedMarca = marcaRepository.save(marca);

        log.info("Marca creada exitosamente con ID: {}", savedMarca.getId());
        return marcaMapper.toDto(savedMarca);
    }

    @Override
    @Transactional(readOnly = true)
    public MarcaResponseDto getById(Integer id) {
        log.info("Buscando marca con ID: {}", id);

        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", id));

        return marcaMapper.toDto(marca);
    }

    @Override
    public MarcaResponseDto update(Integer id, MarcaRequestDto requestDto) {
        log.info("Actualizando marca con ID: {}", id);

        // Verificar si existe la marca
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", id));

        // Verificar si existe otra marca con el mismo nombre (que no sea la actual)
        if (!marca.getNombre().equals(requestDto.nombre()) &&
                marcaRepository.existsByNombre(requestDto.nombre())) {
            throw new BadRequestException("Ya existe otra marca con el nombre: " + requestDto.nombre());
        }

        // Actualizar la entidad y guardar
        marcaMapper.updateEntityFromDto(requestDto, marca);
        Marca updatedMarca = marcaRepository.save(marca);

        log.info("Marca actualizada exitosamente: {}", updatedMarca.getId());
        return marcaMapper.toDto(updatedMarca);
    }

    @Override
    public void delete(Integer id) {
        log.info("Eliminando marca con ID: {}", id);

        // Verificar si existe la marca
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", id));

        // Desactivar la marca en lugar de eliminarla físicamente
        marca.setActivo(false);
        marcaRepository.save(marca);

        log.info("Marca desactivada exitosamente: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponseDto> getAll() {
        log.info("Obteniendo todas las marcas");

        List<Marca> marcas = marcaRepository.findAll(Sort.by("nombre"));
        return marcaMapper.toDtoList(marcas);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<MarcaResponseDto> getPaginated(int page, int size) {
        log.info("Obteniendo marcas paginadas: página {}, tamaño {}", page, size);

        Page<Marca> marcasPage = marcaRepository.findAll(
                PageRequest.of(page, size, Sort.by("nombre")));

        List<MarcaResponseDto> content = marcaMapper.toDtoList(marcasPage.getContent());

        return new PageResponseDto<>(
                content,
                marcasPage.getNumber(),
                marcasPage.getSize(),
                marcasPage.getTotalElements(),
                marcasPage.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaSimpleDto> getAllSimple() {
        log.info("Obteniendo lista simple de todas las marcas");

        List<Marca> marcas = marcaRepository.findByActivoTrue();
        return marcaMapper.toSimpleDtoList(marcas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponseDto> findByNombre(String nombre) {
        log.info("Buscando marcas por nombre: {}", nombre);

        List<Marca> marcas = marcaRepository.findByNombreContainingIgnoreCase(nombre);
        return marcaMapper.toDtoList(marcas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponseDto> findByPaisOrigen(String paisOrigen) {
        log.info("Buscando marcas por país de origen: {}", paisOrigen);

        List<Marca> marcas = marcaRepository.findByPaisOrigenContainingIgnoreCase(paisOrigen);
        return marcaMapper.toDtoList(marcas);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        log.info("Verificando si existe marca con nombre: {}", nombre);
        return marcaRepository.existsByNombre(nombre);
    }
}
