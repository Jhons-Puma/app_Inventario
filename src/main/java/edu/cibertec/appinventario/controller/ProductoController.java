package edu.cibertec.appinventario.controller;

import edu.cibertec.appinventario.dto.PageResponseDto;
import edu.cibertec.appinventario.dto.ProductoRequestDto;
import edu.cibertec.appinventario.dto.ProductoResponseDto;
import edu.cibertec.appinventario.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Productos", description = "API para la gestión de productos del inventario")
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Crear nuevo producto")
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PostMapping
    public ResponseEntity<ProductoResponseDto> create(
            @Valid @RequestBody ProductoRequestDto requestDto) {

        log.info("Solicitud para crear un nuevo producto: {}", requestDto.nombre());
        ProductoResponseDto createdProducto = productoService.create(requestDto);
        return new ResponseEntity<>(createdProducto, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener producto por ID")
            @ApiResponse(responseCode = "200", description = "Producto encontrado")
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> getById(
            @Parameter(description = "ID del producto") @PathVariable Integer id) {

        log.info("Solicitud para obtener producto con ID: {}", id);
        ProductoResponseDto producto = productoService.getById(id);
        return ResponseEntity.ok(producto);
    }

    @Operation(summary = "Actualizar producto existente")
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente")
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> update(
            @Parameter(description = "ID del producto") @PathVariable Integer id,
            @Valid @RequestBody ProductoRequestDto requestDto) {

        log.info("Solicitud para actualizar producto con ID: {}", id);
        ProductoResponseDto updatedProducto = productoService.update(id, requestDto);
        return ResponseEntity.ok(updatedProducto);
    }

    @Operation(summary = "Eliminar producto")
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente")
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del producto") @PathVariable Integer id) {

        log.info("Solicitud para eliminar producto con ID: {}", id);
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos")
    @GetMapping
    public ResponseEntity<List<ProductoResponseDto>> getAll() {

        log.info("Solicitud para obtener todos los productos");
        List<ProductoResponseDto> productos = productoService.getAll();
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener productos paginados")
    @ApiResponse(responseCode = "200", description = "Resultado paginado de productos")
    @GetMapping("/paginados")
    public ResponseEntity<PageResponseDto<ProductoResponseDto>> getPaginated(
            @Parameter(description = "Número de página (desde 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size) {

        log.info("Solicitud para obtener productos paginados: página {}, tamaño {}", page, size);
        PageResponseDto<ProductoResponseDto> productosPaginados = productoService.getPaginated(page, size);
        return ResponseEntity.ok(productosPaginados);
    }

    @Operation(summary = "Buscar producto por código")
            @ApiResponse(responseCode = "200", description = "Producto encontrado")
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoResponseDto> findByCodigo(
            @Parameter(description = "Código del producto") @PathVariable String codigo) {

        log.info("Solicitud para buscar producto por código: {}", codigo);
        ProductoResponseDto producto = productoService.findByCodigo(codigo);
        return ResponseEntity.ok(producto);
    }

    @Operation(summary = "Buscar productos por nombre")
    @ApiResponse(responseCode = "200", description = "Productos que coinciden con el criterio de búsqueda")
    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<ProductoResponseDto>> findByNombre(
            @Parameter(description = "Nombre o parte del nombre a buscar")
            @RequestParam String nombre) {

        log.info("Solicitud para buscar productos por nombre: {}", nombre);
        List<ProductoResponseDto> productos = productoService.findByNombre(nombre);
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Buscar productos por categoría")
    @ApiResponse(responseCode = "200", description = "Productos que pertenecen a la categoría especificada")
    @GetMapping("/por-categoria/{categoriaId}")
    public ResponseEntity<List<ProductoResponseDto>> findByCategoria(
            @Parameter(description = "ID de la categoría") @PathVariable Integer categoriaId) {

        log.info("Solicitud para buscar productos por categoría ID: {}", categoriaId);
        List<ProductoResponseDto> productos = productoService.findByCategoria(categoriaId);
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Buscar productos por marca")
    @ApiResponse(responseCode = "200", description = "Productos que pertenecen a la marca especificada")
    @GetMapping("/por-marca/{marcaId}")
    public ResponseEntity<List<ProductoResponseDto>> findByMarca(
            @Parameter(description = "ID de la marca") @PathVariable Integer marcaId) {

        log.info("Solicitud para buscar productos por marca ID: {}", marcaId);
        List<ProductoResponseDto> productos = productoService.findByMarca(marcaId);
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Buscar productos por rango de precio")
    @ApiResponse(responseCode = "200", description = "Productos dentro del rango de precio especificado")
    @GetMapping("/por-rango-precio")
    public ResponseEntity<List<ProductoResponseDto>> findByRangoPrecio(
            @Parameter(description = "Precio mínimo") @RequestParam BigDecimal precioMin,
            @Parameter(description = "Precio máximo") @RequestParam BigDecimal precioMax) {

        log.info("Solicitud para buscar productos por rango de precio: {} - {}", precioMin, precioMax);
        List<ProductoResponseDto> productos = productoService.findByRangoPrecio(precioMin, precioMax);
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener productos con stock disponible")
    @ApiResponse(responseCode = "200", description = "Productos con stock mayor a cero")
    @GetMapping("/con-stock")
    public ResponseEntity<List<ProductoResponseDto>> findProductosConStock() {

        log.info("Solicitud para obtener productos con stock disponible");
        List<ProductoResponseDto> productos = productoService.findProductosConStock();
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Verificar si existe un producto por código")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    @GetMapping("/existe-codigo")
    public ResponseEntity<Boolean> existsByCodigo(
            @Parameter(description = "Código exacto a verificar")
            @RequestParam String codigo) {

        log.info("Solicitud para verificar si existe producto con código: {}", codigo);
        boolean exists = productoService.existsByCodigo(codigo);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Actualizar stock de un producto")
            @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente")
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o stock insuficiente")
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @PatchMapping("/{id}/actualizar-stock")
    public ResponseEntity<ProductoResponseDto> actualizarStock(
            @Parameter(description = "ID del producto") @PathVariable Integer id,
            @Parameter(description = "Cantidad a agregar (positivo) o restar (negativo)")
            @RequestParam Integer cantidad) {

        log.info("Solicitud para actualizar stock del producto ID: {} en: {}", id, cantidad);
        ProductoResponseDto updatedProducto = productoService.actualizarStock(id, cantidad);
        return ResponseEntity.ok(updatedProducto);
    }
}
