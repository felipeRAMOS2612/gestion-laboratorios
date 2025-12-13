package com.gestion_laboratorios.asignacion_lab.exception;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ConflictException laboratorioOcupado() {
        return new ConflictException("El laboratorio no está disponible en el horario solicitado");
    }

    public static ConflictException asignacionNoModificable() {
        return new ConflictException("La asignación no se puede modificar en su estado actual");
    }

    public static ConflictException horarioConflicto() {
        return new ConflictException("Ya existe una asignación en el horario solicitado");
    }

    public static ConflictException laboratorioEnMantenimiento() {
        return new ConflictException("El laboratorio está en mantenimiento");
    }

    public static ConflictException duplicateResource(String resource) {
        return new ConflictException("Ya existe un recurso duplicado: " + resource);
    }
}