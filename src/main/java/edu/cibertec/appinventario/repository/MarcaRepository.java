package edu.cibertec.appinventario.repository;

import edu.cibertec.appinventario.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {

    // Buscar por nombre (exact match)
    Optional<Marca> findByNombre(String nombre);

    // Buscar por nombre conteniendo cierta cadena (case insensitive)
    List<Marca> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por país de origen
    List<Marca> findByPaisOrigenContainingIgnoreCase(String paisOrigen);

    // Buscar marcas activas
    List<Marca> findByActivoTrue();

    // Verificar si existe por nombre
    boolean existsByNombre(String nombre);
}
