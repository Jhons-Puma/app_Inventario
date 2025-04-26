package edu.cibertec.appinventario.controller;

import edu.cibertec.appinventario.dto.MarcaRequestDto;
import edu.cibertec.appinventario.dto.MarcaResponseDto;
import edu.cibertec.appinventario.dto.MarcaSimpleDto;
import edu.cibertec.appinventario.dto.PageResponseDto;
import edu.cibertec.appinventario.service.MarcaService;
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

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Marcas", description = "API para la gestión de marcas de productos")
public class MarcaController {

    private final MarcaService marcaService;

    @Operation(summary = "Crear nueva marca")
        @ApiResponse(responseCode = "201", description = "Marca creada exitosamente")
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PostMapping
    public ResponseEntity<MarcaResponseDto> create(
            @Valid @RequestBody MarcaRequestDto requestDto) {

        log.info("Solicitud para crear una nueva marca: {}", requestDto.nombre());
        MarcaResponseDto createdMarca = marcaService.create(requestDto);
        return new ResponseEntity<>(createdMarca, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener marca por ID")
        @ApiResponse(responseCode = "201", description = "Marca creada exitosamente")
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponseDto> getById(
            @Parameter(description = "ID de la marca") @PathVariable Integer id) {

        log.info("Solicitud para obtener marca con ID: {}", id);
        MarcaResponseDto marca = marcaService.getById(id);
        return ResponseEntity.ok(marca);
    }

    @Operation(summary = "Actualizar marca existente")

        @ApiResponse(responseCode = "200", description = "Marca actualizada exitosamente")
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
        @ApiResponse(responseCode = "404", description = "Marca no encontrada")

    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponseDto> update(
            @Parameter(description = "ID de la marca") @PathVariable Integer id,
            @Valid @RequestBody MarcaRequestDto requestDto) {

        log.info("Solicitud para actualizar marca con ID: {}", id);
        MarcaResponseDto updatedMarca = marcaService.update(id, requestDto);
        return ResponseEntity.ok(updatedMarca);
    }

    @Operation(summary = "Eliminar marca")

            @ApiResponse(responseCode = "204", description = "Marca eliminada exitosamente")
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la marca") @PathVariable Integer id) {

        log.info("Solicitud para eliminar marca con ID: {}", id);
        marcaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener todas las marcas")
    @ApiResponse(responseCode = "200", description = "Lista de marcas")
    @GetMapping
    public ResponseEntity<List<MarcaResponseDto>> getAll() {

        log.info("Solicitud para obtener todas las marcas");
        List<MarcaResponseDto> marcas = marcaService.getAll();
        return ResponseEntity.ok(marcas);
    }

    @Operation(summary = "Obtener marcas paginadas")
    @ApiResponse(responseCode = "200", description = "Resultado paginado de marcas")
    @GetMapping("/paginadas")
    public ResponseEntity<PageResponseDto<MarcaResponseDto>> getPaginated(
            @Parameter(description = "Número de página (desde 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size) {

        log.info("Solicitud para obtener marcas paginadas: página {}, tamaño {}", page, size);
        PageResponseDto<MarcaResponseDto> marcasPaginadas = marcaService.getPaginated(page, size);
        return ResponseEntity.ok(marcasPaginadas);
    }

    @Operation(summary = "Obtener lista simple de todas las marcas activas")
    @ApiResponse(responseCode = "200", description = "Lista simple de marcas")
    @GetMapping("/simple")
    public ResponseEntity<List<MarcaSimpleDto>> getAllSimple() {

        log.info("Solicitud para obtener lista simple de marcas");
        List<MarcaSimpleDto> marcas = marcaService.getAllSimple();
        return ResponseEntity.ok(marcas);
    }

    @Operation(summary = "Buscar marcas por nombre")
    @ApiResponse(responseCode = "200", description = "Marcas que coinciden con el criterio de búsqueda")
    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<MarcaResponseDto>> findByNombre(
            @Parameter(description = "Nombre o parte del nombre a buscar")
            @RequestParam String nombre) {

        log.info("Solicitud para buscar marcas por nombre: {}", nombre);
        List<MarcaResponseDto> marcas = marcaService.findByNombre(nombre);
        return ResponseEntity.ok(marcas);
    }

    @Operation(summary = "Buscar marcas por país de origen")
    @ApiResponse(responseCode = "200", description = "Marcas que coinciden con el criterio de búsqueda")
    @GetMapping("/buscar-por-pais")
    public ResponseEntity<List<MarcaResponseDto>> findByPaisOrigen(
            @Parameter(description = "País de origen o parte del mismo a buscar")
            @RequestParam String paisOrigen) {

        log.info("Solicitud para buscar marcas por país de origen: {}", paisOrigen);
        List<MarcaResponseDto> marcas = marcaService.findByPaisOrigen(paisOrigen);
        return ResponseEntity.ok(marcas);
    }

    @Operation(summary = "Verificar si existe una marca por nombre")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existsByNombre(
            @Parameter(description = "Nombre exacto a verificar")
            @RequestParam String nombre) {

        log.info("Solicitud para verificar si existe marca con nombre: {}", nombre);
        boolean exists = marcaService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }
}
