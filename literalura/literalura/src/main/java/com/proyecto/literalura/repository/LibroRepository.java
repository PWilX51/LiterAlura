package com.proyecto.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.literalura.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long>{
}