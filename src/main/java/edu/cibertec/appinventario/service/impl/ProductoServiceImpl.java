package edu.cibertec.appinventario.service.impl;

import edu.cibertec.appinventario.dto.PageResponseDto;
import edu.cibertec.appinventario.dto.ProductoRequestDto;
import edu.cibertec.appinventario.dto.ProductoResponseDto;
import edu.cibertec.appinventario.exception.BadRequestException;
import edu.cibertec.appinventario.exception.ResourceNotFoundException;
import edu.cibertec.appinventario.mapper.ProductoMapper;
import edu.cibertec.appinventario.model.Producto;
import edu.cibertec.appinventario.repository.CategoriaRepository;
import edu.cibertec.appinventario.repository.MarcaRepository;
import edu.cibertec.appinventario.repository.ProductoRepository;
import edu.cibertec.appinventario.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProductoMapper productoMapper;

    @Override
    public ProductoResponseDto create(ProductoRequestDto requestDto) {
        log.info("Creando nuevo producto con código: {}", requestDto.codigo());

        // Verificar si ya existe un producto con el mismo código
        if (productoRepository.existsByCodigo(requestDto.codigo())) {
            throw new BadRequestException("Ya existe un producto con el código: " + requestDto.codigo());
        }

        // Verificar si existen la categoría y la marca
        verificarCategoriaExiste(requestDto.categoriaId());
        verificarMarcaExiste(requestDto.marcaId());

        // Convertir DTO a entidad, guardar y convertir resultado a DTO de respuesta
        Producto producto = productoMapper.toEntity(requestDto);
        Producto savedProducto = productoRepository.save(producto);

        log.info("Producto creado exitosamente con ID: {}", savedProducto.getId());
        return productoMapper.toDto(savedProducto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDto getById(Integer id) {
        log.info("Buscando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        return productoMapper.toDto(producto);
    }

    @Override
    public ProductoResponseDto update(Integer id, ProductoRequestDto requestDto) {
        log.info("Actualizando producto con ID: {}", id);

        // Verificar si existe el producto
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        // Verificar si existe otro producto con el mismo código (que no sea el actual)
        if (!producto.getCodigo().equals(requestDto.codigo()) &&
                productoRepository.existsByCodigo(requestDto.codigo())) {
            throw new BadRequestException("Ya existe otro producto con el código: " + requestDto.codigo());
        }

        // Verificar si existen la categoría y la marca
        verificarCategoriaExiste(requestDto.categoriaId());
        verificarMarcaExiste(requestDto.marcaId());

        // Actualizar la entidad y guardar
        productoMapper.updateEntityFromDto(requestDto, producto);
        Producto updatedProducto = productoRepository.save(producto);

        log.info("Producto actualizado exitosamente: {}", updatedProducto.getId());
        return productoMapper.toDto(updatedProducto);
    }

    @Override
    public void delete(Integer id) {
        log.info("Eliminando producto con ID: {}", id);

        // Verificar si existe el producto
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        // Desactivar el producto en lugar de eliminarlo físicamente
        producto.setActivo(false);
        productoRepository.save(producto);

        log.info("Producto desactivado exitosamente: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> getAll() {
        log.info("Obteniendo todos los productos");

        List<Producto> productos = productoRepository.findAll(Sort.by("nombre"));
        return productoMapper.toDtoList(productos);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<ProductoResponseDto> getPaginated(int page, int size) {
        log.info("Obteniendo productos paginados: página {}, tamaño {}", page, size);

        Page<Producto> productosPage = productoRepository.findAll(
                PageRequest.of(page, size, Sort.by("nombre")));

        List<ProductoResponseDto> content = productoMapper.toDtoList(productosPage.getContent());

        return new PageResponseDto<>(
                content,
                productosPage.getNumber(),
                productosPage.getSize(),
                productosPage.getTotalElements(),
                productosPage.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDto findByCodigo(String codigo) {
        log.info("Buscando producto con código: {}", codigo);

        Producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "código", codigo));

        return productoMapper.toDto(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> findByNombre(String nombre) {
        log.info("Buscando productos por nombre: {}", nombre);

        List<Producto> productos = productoRepository.findByNombreContainingIgnoreCase(nombre);
        return productoMapper.toDtoList(productos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> findByCategoria(Integer categoriaId) {
        log.info("Buscando productos por categoría ID: {}", categoriaId);

        // Verificar si existe la categoría
        verificarCategoriaExiste(categoriaId);

        List<Producto> productos = productoRepository.findByCategoriaId(categoriaId);
        return productoMapper.toDtoList(productos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> findByMarca(Integer marcaId) {
        log.info("Buscando productos por marca ID: {}", marcaId);

        // Verificar si existe la marca
        verificarMarcaExiste(marcaId);

        List<Producto> productos = productoRepository.findByMarcaId(marcaId);
        return productoMapper.toDtoList(productos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> findByRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        log.info("Buscando productos por rango de precio: {} - {}", precioMin, precioMax);

        if (precioMin == null || precioMax == null || precioMin.compareTo(precioMax) > 0) {
            throw new BadRequestException("El rango de precios no es válido");
        }

        List<Producto> productos = productoRepository.findByPrecioBetween(precioMin, precioMax);
        return productoMapper.toDtoList(productos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> findProductosConStock() {
        log.info("Buscando productos con stock disponible");

        List<Producto> productos = productoRepository.findByStockGreaterThan(0);
        return productoMapper.toDtoList(productos);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCodigo(String codigo) {
        log.info("Verificando si existe producto con código: {}", codigo);
        return productoRepository.existsByCodigo(codigo);
    }

    @Override
    public ProductoResponseDto actualizarStock(Integer id, Integer cantidad) {
        log.info("Actualizando stock del producto ID: {} en: {}", id, cantidad);

        // Verificar si existe el producto
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        // Validar que el stock no quede negativo
        int nuevoStock = producto.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new BadRequestException("No hay suficiente stock disponible");
        }

        // Actualizar el stock
        producto.setStock(nuevoStock);
        Producto updatedProducto = productoRepository.save(producto);

        log.info("Stock actualizado exitosamente para producto ID: {}, nuevo stock: {}",
                id, updatedProducto.getStock());

        return productoMapper.toDto(updatedProducto);
    }

    // Métodos privados de utilidad

    private void verificarCategoriaExiste(Integer categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new ResourceNotFoundException("Categoria", "id", categoriaId);
        }
    }

    private void verificarMarcaExiste(Integer marcaId) {
        if (!marcaRepository.existsById(marcaId)) {
            throw new ResourceNotFoundException("Marca", "id", marcaId);
        }
    }
}
