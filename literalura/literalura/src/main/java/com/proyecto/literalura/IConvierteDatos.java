package com.proyecto.literalura;

public interface IConvierteDatos {
    default  <T> T convertir(String json, Class<T> clase){
        return null;
    }
}