package com.gestion_laboratorios.usuarios.exception;

public class DuplicateResourceException extends RuntimeException {
    
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static DuplicateResourceException username(String username) {
        return new DuplicateResourceException("Ya existe un usuario con el username: " + username);
    }
    
    public static DuplicateResourceException email(String email) {
        return new DuplicateResourceException("Ya existe un usuario con el email: " + email);
    }
}