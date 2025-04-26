package edu.cibertec.appinventario.repository;

import edu.cibertec.appinventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Buscar por código
    Optional<Producto> findByCodigo(String codigo);

    // Buscar por nombre conteniendo cierta cadena
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por categoría
    List<Producto> findByCategoriaId(Integer categoriaId);

    // Buscar por marca
    List<Producto> findByMarcaId(Integer marcaId);

    // Buscar productos con stock > 0
    List<Producto> findByStockGreaterThan(Integer stockMinimo);

    // Buscar productos por rango de precio
    List<Producto> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);

    // Buscar productos activos
    List<Producto> findByActivoTrue();

    // Verificar si existe por código
    boolean existsByCodigo(String codigo);

    // Consulta JPQL personalizada para buscar productos por nombre de categoría
    @Query("SELECT p FROM Producto p JOIN p.categoria c WHERE c.nombre = :categoriaNombre")
    List<Producto> findByCategoriaNombre(@Param("categoriaNombre") String categoriaNombre);

    // Consulta JPQL personalizada para buscar productos por nombre de marca
    @Query("SELECT p FROM Producto p JOIN p.marca m WHERE m.nombre = :marcaNombre")
    List<Producto> findByMarcaNombre(@Param("marcaNombre") String marcaNombre);
}
