package com.gestion_laboratorios.usuarios.service;

import com.gestion_laboratorios.usuarios.dto.*;
import com.gestion_laboratorios.usuarios.entity.Usuario;
import com.gestion_laboratorios.usuarios.exception.DuplicateResourceException;
import com.gestion_laboratorios.usuarios.exception.InvalidCredentialsException;
import com.gestion_laboratorios.usuarios.exception.UsuarioNotFoundException;
import com.gestion_laboratorios.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponseDto crearUsuario(UsuarioRequestDto request) {
        log.info("Creando nuevo usuario con username: {}", request.getUsername());
        
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw DuplicateResourceException.username(request.getUsername());
        }
        
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw DuplicateResourceException.email(request.getEmail());
        }

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .tipoUsuario(request.getTipoUsuario())
                .activo(true)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Usuario creado exitosamente con ID: {}", usuarioGuardado.getId());
        
        return mapToResponseDto(usuarioGuardado);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> obtenerTodosLosUsuarios() {
        log.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> obtenerUsuariosActivos() {
        log.info("Obteniendo usuarios activos");
        return usuarioRepository.findByActivoTrue()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDto obtenerUsuarioPorId(Long id) {
        log.info("Buscando usuario por ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> UsuarioNotFoundException.porId(id));
        
        return mapToResponseDto(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDto obtenerUsuarioPorUsername(String username) {
        log.info("Buscando usuario por username: {}", username);
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> UsuarioNotFoundException.porUsername(username));
        
        return mapToResponseDto(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> obtenerUsuariosPorTipo(Usuario.TipoUsuario tipoUsuario) {
        log.info("Obteniendo usuarios por tipo: {}", tipoUsuario);
        return usuarioRepository.findByActivoTrueAndTipoUsuario(tipoUsuario)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> buscarUsuarios(String searchTerm) {
        log.info("Buscando usuarios con término: {}", searchTerm);
        return usuarioRepository.findBySearchTerm(searchTerm)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioResponseDto actualizarUsuario(Long id, UsuarioUpdateDto updateDto) {
        log.info("Actualizando usuario con ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> UsuarioNotFoundException.porId(id));

        if (updateDto.getEmail() != null && !updateDto.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(updateDto.getEmail())) {
                throw DuplicateResourceException.email(updateDto.getEmail());
            }
            usuario.setEmail(updateDto.getEmail());
        }

        if (updateDto.getNombre() != null) {
            usuario.setNombre(updateDto.getNombre());
        }
        
        if (updateDto.getApellido() != null) {
            usuario.setApellido(updateDto.getApellido());
        }
        
        if (updateDto.getTipoUsuario() != null) {
            usuario.setTipoUsuario(updateDto.getTipoUsuario());
        }
        
        if (updateDto.getActivo() != null) {
            usuario.setActivo(updateDto.getActivo());
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        log.info("Usuario actualizado exitosamente con ID: {}", usuarioActualizado.getId());
        
        return mapToResponseDto(usuarioActualizado);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> UsuarioNotFoundException.porId(id));
        
        usuarioRepository.delete(usuario);
        log.info("Usuario eliminado exitosamente con ID: {}", id);
    }

    @Transactional
    public void desactivarUsuario(Long id) {
        log.info("Desactivando usuario con ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> UsuarioNotFoundException.porId(id));
        
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        log.info("Usuario desactivado exitosamente con ID: {}", id);
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto loginRequest) {
        log.info("Intento de login para usuario: {}", loginRequest.getUsername());
        
        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(InvalidCredentialsException::credencialesIncorrectas);

        // Validar contraseña (en producción, comparar hash)
        if (!usuario.getPassword().equals(loginRequest.getPassword())) {
            log.warn("Contraseña incorrecta para usuario: {}", loginRequest.getUsername());
            throw InvalidCredentialsException.credencialesIncorrectas();
        }

        // Validar que el usuario esté activo
        if (!usuario.getActivo()) {
            log.warn("Usuario inactivo intentando login: {}", loginRequest.getUsername());
            throw InvalidCredentialsException.usuarioInactivo();
        }

        log.info("Login exitoso para usuario: {}", loginRequest.getUsername());
        
        return LoginResponseDto.builder()
                .mensaje("Login exitoso")
                .usuario(mapToResponseDto(usuario))
                .token("JWT_TOKEN_PLACEHOLDER") // Implementar JWT en el futuro
                .build();
    }

    private UsuarioResponseDto mapToResponseDto(Usuario usuario) {
        return UsuarioResponseDto.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .tipoUsuario(usuario.getTipoUsuario())
                .activo(usuario.getActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .fechaActualizacion(usuario.getFechaActualizacion())
                .build();
    }
}