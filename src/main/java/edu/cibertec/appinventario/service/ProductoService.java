package edu.cibertec.appinventario.service;

import edu.cibertec.appinventario.dto.PageResponseDto;
import edu.cibertec.appinventario.dto.ProductoRequestDto;
import edu.cibertec.appinventario.dto.ProductoResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {

    // Operaciones CRUD básicas
    ProductoResponseDto create(ProductoRequestDto requestDto);
    ProductoResponseDto getById(Integer id);
    ProductoResponseDto update(Integer id, ProductoRequestDto requestDto);
    void delete(Integer id);

    // Operaciones adicionales
    List<ProductoResponseDto> getAll();
    PageResponseDto<ProductoResponseDto> getPaginated(int page, int size);
    ProductoResponseDto findByCodigo(String codigo);
    List<ProductoResponseDto> findByNombre(String nombre);
    List<ProductoResponseDto> findByCategoria(Integer categoriaId);
    List<ProductoResponseDto> findByMarca(Integer marcaId);
    List<ProductoResponseDto> findByRangoPrecio(BigDecimal precioMin, BigDecimal precioMax);
    List<ProductoResponseDto> findProductosConStock();
    boolean existsByCodigo(String codigo);

    // Actualización de stock
    ProductoResponseDto actualizarStock(Integer id, Integer cantidad);
}
