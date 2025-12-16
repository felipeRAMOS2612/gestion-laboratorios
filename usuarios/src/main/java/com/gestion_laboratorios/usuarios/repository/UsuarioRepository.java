package com.gestion_laboratorios.usuarios.repository;

import com.gestion_laboratorios.usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);

    boolean existsByTipoUsuario(Usuario.TipoUsuario tipoUsuario);
    
    List<Usuario> findByActivoTrue();
    
    List<Usuario> findByTipoUsuario(Usuario.TipoUsuario tipoUsuario);
    
    @Query("SELECT u FROM Usuario u WHERE u.activo = true AND u.tipoUsuario = :tipoUsuario")
    List<Usuario> findByActivoTrueAndTipoUsuario(@Param("tipoUsuario") Usuario.TipoUsuario tipoUsuario);
    
    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.apellido) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Usuario> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    Optional<Usuario> findByResetPasswordToken(String resetPasswordToken);
}