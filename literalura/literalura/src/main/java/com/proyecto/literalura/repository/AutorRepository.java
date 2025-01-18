package com.proyecto.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.literalura.model.Autor;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long>{
    Optional<Autor> findByNombreContainsIgnoreCase(String nombre);
}