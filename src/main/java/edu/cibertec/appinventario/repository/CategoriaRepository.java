package edu.cibertec.appinventario.repository;

import edu.cibertec.appinventario.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    // Buscar por nombre (exact match)
    Optional<Categoria> findByNombre(String nombre);

    // Buscar por nombre conteniendo cierta cadena (case insensitive)
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    // Buscar categorías activas
    List<Categoria> findByActivoTrue();

    // Verificar si existe por nombre
    boolean existsByNombre(String nombre);
}
