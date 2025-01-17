package com.proyecto.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.literalura.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long>{
}