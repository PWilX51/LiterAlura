package com.proyecto.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.literalura.model.Libro;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long>{
    Optional<Libro> findByTituloContainsIgnoreCase(String nombre);
}