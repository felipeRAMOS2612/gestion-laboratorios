package com.gestion_laboratorios.usuarios.exception;

public class UsuarioNotFoundException extends RuntimeException {
    
    public UsuarioNotFoundException(String message) {
        super(message);
    }
    
    public UsuarioNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static UsuarioNotFoundException porId(Long id) {
        return new UsuarioNotFoundException("Usuario no encontrado con ID: " + id);
    }
    
    public static UsuarioNotFoundException porUsername(String username) {
        return new UsuarioNotFoundException("Usuario no encontrado con username: " + username);
    }
    
    public static UsuarioNotFoundException porEmail(String email) {
        return new UsuarioNotFoundException("Usuario no encontrado con email: " + email);
    }
}