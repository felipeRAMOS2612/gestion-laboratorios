package com.gestion_laboratorios.usuarios.dto;

import com.gestion_laboratorios.usuarios.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioUpdateDto {

    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;

    private Usuario.TipoUsuario tipoUsuario;

    private Boolean activo;
}