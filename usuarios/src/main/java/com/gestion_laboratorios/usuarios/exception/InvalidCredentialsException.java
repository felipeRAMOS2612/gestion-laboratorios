package com.gestion_laboratorios.usuarios.exception;

public class InvalidCredentialsException extends RuntimeException {
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
    
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static InvalidCredentialsException credencialesIncorrectas() {
        return new InvalidCredentialsException("Credenciales incorrectas");
    }
    
    public static InvalidCredentialsException usuarioInactivo() {
        return new InvalidCredentialsException("Usuario inactivo");
    }
}