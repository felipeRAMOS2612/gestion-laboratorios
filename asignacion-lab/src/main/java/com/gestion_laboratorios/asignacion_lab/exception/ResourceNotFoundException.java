package com.gestion_laboratorios.asignacion_lab.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ResourceNotFoundException laboratorio(Long id) {
        return new ResourceNotFoundException("Laboratorio no encontrado con ID: " + id);
    }

    public static ResourceNotFoundException tipoAnalisis(Long id) {
        return new ResourceNotFoundException("Tipo de análisis no encontrado con ID: " + id);
    }

    public static ResourceNotFoundException asignacion(Long id) {
        return new ResourceNotFoundException("Asignación no encontrada con ID: " + id);
    }

    public static ResourceNotFoundException usuario(Long id) {
        return new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
    }
}