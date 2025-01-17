package com.proyecto.literalura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.proyecto.literalura.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import com.proyecto.literalura.repository.LibroRepository;
import com.proyecto.literalura.repository.AutorRepository;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner{
	@Autowired
	private LibroRepository repository;
	@Autowired
	private AutorRepository autorRepository;
	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String[] args) throws Exception {
		Principal principal = new Principal(repository, autorRepository);
		principal.muestraMenu();
	}
}
