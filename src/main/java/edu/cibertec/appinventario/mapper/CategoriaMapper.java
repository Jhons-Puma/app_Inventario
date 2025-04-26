package edu.cibertec.appinventario.mapper;

import edu.cibertec.appinventario.dto.CategoriaRequestDto;
import edu.cibertec.appinventario.dto.CategoriaResponseDto;
import edu.cibertec.appinventario.dto.CategoriaSimpleDto;
import edu.cibertec.appinventario.model.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoriaMapper {

    // De DTO de petición a entidad (para creación)
    Categoria toEntity(CategoriaRequestDto dto);

    // De entidad a DTO de respuesta completa
    CategoriaResponseDto toDto(Categoria entity);

    // De entidad a DTO simple (para relaciones)
    CategoriaSimpleDto toSimpleDto(Categoria entity);

    // Para actualizar una entidad existente con un DTO
    void updateEntityFromDto(CategoriaRequestDto dto, @MappingTarget Categoria entity);

    // Para convertir listas de entidades a listas de DTOs
    List<CategoriaResponseDto> toDtoList(List<Categoria> entities);
    List<CategoriaSimpleDto> toSimpleDtoList(List<Categoria> entities);
}
