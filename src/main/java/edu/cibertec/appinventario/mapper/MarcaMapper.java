package edu.cibertec.appinventario.mapper;

import edu.cibertec.appinventario.dto.MarcaRequestDto;
import edu.cibertec.appinventario.dto.MarcaResponseDto;
import edu.cibertec.appinventario.dto.MarcaSimpleDto;
import edu.cibertec.appinventario.model.Marca;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MarcaMapper {

    // De DTO de petición a entidad (para creación)
    Marca toEntity(MarcaRequestDto dto);

    // De entidad a DTO de respuesta completa
    MarcaResponseDto toDto(Marca entity);

    // De entidad a DTO simple (para relaciones)
    MarcaSimpleDto toSimpleDto(Marca entity);

    // Para actualizar una entidad existente con un DTO
    void updateEntityFromDto(MarcaRequestDto dto, @MappingTarget Marca entity);

    // Para convertir listas de entidades a listas de DTOs
    List<MarcaResponseDto> toDtoList(List<Marca> entities);
    List<MarcaSimpleDto> toSimpleDtoList(List<Marca> entities);
}
