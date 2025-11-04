package com.gestion_laboratorios.usuarios.dto;

import com.gestion_laboratorios.usuarios.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDto {

    private Long id;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private Usuario.TipoUsuario tipoUsuario;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}