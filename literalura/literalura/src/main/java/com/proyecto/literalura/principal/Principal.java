package com.proyecto.literalura.principal;

import java.util.Scanner;
import com.proyecto.literalura.model.DatosLibros;
import com.proyecto.literalura.model.Libro;
import com.proyecto.literalura.model.Autor;
import com.proyecto.literalura.model.Resultados;
import com.proyecto.literalura.ConsumoAPI;
import com.proyecto.literalura.ConvierteDatos;
import com.proyecto.literalura.repository.LibroRepository;
import com.proyecto.literalura.repository.AutorRepository;
import java.util.stream.Collectors;
import java.util.*;

public class Principal {
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private String URL_BASE = "https://gutendex.com/books";
    private List<Libro> libros = new ArrayList<>();
    private List<Autor> autores = new ArrayList<>();
    private LibroRepository repository;
    private AutorRepository autorRepository;
    private Scanner teclado = new Scanner(System.in);

    public Principal(LibroRepository repository, AutorRepository autorRepository){
        this.repository = repository;
        this.autorRepository = autorRepository;
    }

    public void muestraMenu(){
        var opcion=-1;
        while(opcion!=0){
            String menu= """
                    \u001B[32mElige una opcion:
                    
                    1. Buscar libro
                    2. Mostrar todos los libros 
                    3. Buscar autor
                    4. Buscar libros por idioma
                    5. Autores vivos por anio
                    0. Salir\u001B[0m
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch(opcion){
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibros();
                    break;
                case 3:
                    buscarAutores();
                    break;
                case 4:
                    buscarLibrosPorIdioma();
                    break;
                case 5:
                    buscarAutoresVivosPorAnio();
                    break;
                default:
                    System.out.println("Este campo no existe.");
                    break;
            }
        }
    }

    private void buscarLibroPorTitulo(){
        System.out.println("Digita el nombre de un libro");
        var search = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE+"/?search="+search.replace(" ","%20"));
        Resultados resultados = conversor.convertir(json, Resultados.class);
        DatosLibros datos = resultados.datosLibrosList().get(0);
        Autor autor = new Autor(datos.datosAutoresList().get(0));
        Libro libro = new Libro(datos);
        Optional<Autor> autorEncontrado = autorRepository.findByNombreContainsIgnoreCase(autor.getNombre());
        Optional<Libro> libroEncontrado = repository.findByTituloContainsIgnoreCase(libro.getTitulo());

        if(autorEncontrado.isPresent()) {
            System.out.println("Autor ya registrado");
            libro.setAutor(autorEncontrado.get());
        }else {
            autorRepository.save(autor);
            System.out.println("Autor guardado correctamente.");
            libro.setAutor(autor);
        }

        if(libroEncontrado.isPresent()) {
            System.out.println("Libro ya registrado");
        }else{
            repository.save(libro);
            System.out.println("Libro guardado correctamente");
        }
        System.out.println(libro.toString());
    }

    private void mostrarLibros(){
        libros = repository.findAll();
        libros.forEach(System.out::println);
    }

    private void buscarAutores(){
        autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void buscarLibrosPorIdioma(){
        System.out.println("Digita el idioma");
        var idioma = teclado.nextLine();
        String parametro="";
        if (idioma.equals("ingles")){
            parametro="en";
            System.out.println(parametro);
        }else if(idioma.equals("español")){
            parametro="es";
            System.out.println(parametro);
        }else{
            System.out.println("No hay libros en este idioma");
            return;
        }
        var json = consumoApi.obtenerDatos(URL_BASE+"/?languages="+parametro);
        System.out.println(json);

        Resultados resultados = conversor.convertir(json, Resultados.class);
        System.out.println(resultados.datosLibrosList().size());
        for(int i=0; i<resultados.datosLibrosList().size();i++) {
            DatosLibros datos = resultados.datosLibrosList().get(i);
            if(datos.datosAutoresList().size()>0) {
                Autor autor = new Autor(datos.datosAutoresList().get(0));
                Libro libro = new Libro(datos);
                Optional<Autor> autorEncontrado = autorRepository.findByNombreContainsIgnoreCase(autor.getNombre());
                Optional<Libro> libroEncontrado = repository.findByTituloContainsIgnoreCase(libro.getTitulo());

                if(autorEncontrado.isPresent()) {
                    System.out.println("Autor ya registrado");
                    libro.setAutor(autorEncontrado.get());
                }else {
                    autorRepository.save(autor);
                    System.out.println("Autor guardado correctamente.");
                    libro.setAutor(autor);
                }

                if(libroEncontrado.isPresent()) {
                    System.out.println("Libro ya registrado");
                }else{
                    repository.save(libro);
                    System.out.println("Libro guardado correctamente");
                }
                System.out.println(libro.toString());
            }
        }

        System.out.println("Cantidad de libros: " + resultados.datosLibrosList().size());
    }

    private void buscarAutoresVivosPorAnio(){
        System.out.println("Ingrese un año");
        var anioLimite = teclado.nextInt();
        autores = autorRepository.findAll();
        List<Autor> autoresEncontrados = autores.stream().filter(a -> a.getFechaDeFallecimiento()>=anioLimite).collect(Collectors.toList());
        if(autoresEncontrados.size()>0){
            autoresEncontrados.forEach(System.out::println);
        }else{
            System.out.println("No existe ningun autor vivo en este año");
        }
    }
}