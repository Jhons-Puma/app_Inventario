package edu.cibertec.appinventario.controller;

import edu.cibertec.appinventario.dto.CategoriaRequestDto;
import edu.cibertec.appinventario.dto.CategoriaResponseDto;
import edu.cibertec.appinventario.dto.CategoriaSimpleDto;
import edu.cibertec.appinventario.dto.PageResponseDto;
import edu.cibertec.appinventario.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Categorías", description = "API para la gestión de categorías de productos")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Crear nueva categoría")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<CategoriaResponseDto> create(
            @Valid @RequestBody CategoriaRequestDto requestDto) {

        log.info("Solicitud para crear una nueva categoría: {}", requestDto.nombre());
        CategoriaResponseDto createdCategoria = categoriaService.create(requestDto);
        return new ResponseEntity<>(createdCategoria, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener categoría por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> getById(
            @Parameter(description = "ID de la categoría") @PathVariable Integer id) {

        log.info("Solicitud para obtener categoría con ID: {}", id);
        CategoriaResponseDto categoria = categoriaService.getById(id);
        return ResponseEntity.ok(categoria);
    }

    @Operation(summary = "Actualizar categoría existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> update(
            @Parameter(description = "ID de la categoría") @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequestDto requestDto) {

        log.info("Solicitud para actualizar categoría con ID: {}", id);
        CategoriaResponseDto updatedCategoria = categoriaService.update(id, requestDto);
        return ResponseEntity.ok(updatedCategoria);
    }

    @Operation(summary = "Eliminar categoría")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la categoría") @PathVariable Integer id) {

        log.info("Solicitud para eliminar categoría con ID: {}", id);
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener todas las categorías")
    @ApiResponse(responseCode = "200", description = "Lista de categorías")
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDto>> getAll() {

        log.info("Solicitud para obtener todas las categorías");
        List<CategoriaResponseDto> categorias = categoriaService.getAll();
        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Obtener categorías paginadas")
    @ApiResponse(responseCode = "200", description = "Resultado paginado de categorías")
    @GetMapping("/paginadas")
    public ResponseEntity<PageResponseDto<CategoriaResponseDto>> getPaginated(
            @Parameter(description = "Número de página (desde 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size) {

        log.info("Solicitud para obtener categorías paginadas: página {}, tamaño {}", page, size);
        PageResponseDto<CategoriaResponseDto> categoriasPaginadas = categoriaService.getPaginated(page, size);
        return ResponseEntity.ok(categoriasPaginadas);
    }

    @Operation(summary = "Obtener lista simple de todas las categorías activas")
    @ApiResponse(responseCode = "200", description = "Lista simple de categorías")
    @GetMapping("/simple")
    public ResponseEntity<List<CategoriaSimpleDto>> getAllSimple() {

        log.info("Solicitud para obtener lista simple de categorías");
        List<CategoriaSimpleDto> categorias = categoriaService.getAllSimple();
        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Buscar categorías por nombre")
    @ApiResponse(responseCode = "200", description = "Categorías que coinciden con el criterio de búsqueda")
    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaResponseDto>> findByNombre(
            @Parameter(description = "Nombre o parte del nombre a buscar")
            @RequestParam String nombre) {

        log.info("Solicitud para buscar categorías por nombre: {}", nombre);
        List<CategoriaResponseDto> categorias = categoriaService.findByNombre(nombre);
        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Verificar si existe una categoría por nombre")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existsByNombre(
            @Parameter(description = "Nombre exacto a verificar")
            @RequestParam String nombre) {

        log.info("Solicitud para verificar si existe categoría con nombre: {}", nombre);
        boolean exists = categoriaService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }
}
