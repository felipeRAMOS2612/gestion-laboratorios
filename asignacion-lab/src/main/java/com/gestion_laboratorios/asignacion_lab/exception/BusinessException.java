package com.gestion_laboratorios.asignacion_lab.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public static BusinessException fechaInvalida() {
        return new BusinessException("La fecha de la asignación no puede ser en el pasado");
    }

    public static BusinessException capacidadExcedida() {
        return new BusinessException("Se ha excedido la capacidad máxima del laboratorio");
    }

    public static BusinessException tipoAnalisisInactivo() {
        return new BusinessException("No se pueden crear asignaciones para tipos de análisis inactivos");
    }

    public static BusinessException duracionInvalida() {
        return new BusinessException("La duración del análisis no es válida");
    }
}