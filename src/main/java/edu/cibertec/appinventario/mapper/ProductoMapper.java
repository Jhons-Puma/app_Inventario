package edu.cibertec.appinventario.mapper;

import edu.cibertec.appinventario.dto.ProductoRequestDto;
import edu.cibertec.appinventario.dto.ProductoResponseDto;
import edu.cibertec.appinventario.dto.ProductoSimpleDto;
import edu.cibertec.appinventario.model.Categoria;
import edu.cibertec.appinventario.model.Marca;
import edu.cibertec.appinventario.model.Producto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CategoriaMapper.class, MarcaMapper.class}
)
public interface ProductoMapper {

    // De DTO de petición a entidad (para creación)
    @Mapping(target = "categoria", source = "categoriaId", qualifiedByName = "categoriaIdToCategoria")
    @Mapping(target = "marca", source = "marcaId", qualifiedByName = "marcaIdToMarca")
    Producto toEntity(ProductoRequestDto dto);

    // De entidad a DTO de respuesta completa
    @Mapping(target = "categoria", source = "categoria")
    @Mapping(target = "marca", source = "marca")
    ProductoResponseDto toDto(Producto entity);

    // De entidad a DTO simple
    ProductoSimpleDto toSimpleDto(Producto entity);

    // Para actualizar una entidad existente con un DTO
    @Mapping(target = "categoria", source = "categoriaId", qualifiedByName = "categoriaIdToCategoria")
    @Mapping(target = "marca", source = "marcaId", qualifiedByName = "marcaIdToMarca")
    void updateEntityFromDto(ProductoRequestDto dto, @MappingTarget Producto entity);

    // Para convertir listas de entidades a listas de DTOs
    List<ProductoResponseDto> toDtoList(List<Producto> entities);
    List<ProductoSimpleDto> toSimpleDtoList(List<Producto> entities);

    // Métodos para mapear IDs a entidades
    @Named("categoriaIdToCategoria")
    default Categoria categoriaIdToCategoria(Integer id) {
        if (id == null) {
            return null;
        }
        Categoria categoria = new Categoria();
        categoria.setId(id);
        return categoria;
    }

    @Named("marcaIdToMarca")
    default Marca marcaIdToMarca(Integer id) {
        if (id == null) {
            return null;
        }
        Marca marca = new Marca();
        marca.setId(id);
        return marca;
    }
}
