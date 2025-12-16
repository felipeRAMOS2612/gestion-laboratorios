package com.gestion_laboratorios.usuarios.service;

import com.gestion_laboratorios.usuarios.config.JwtTokenUtil;
import com.gestion_laboratorios.usuarios.dto.*;
import com.gestion_laboratorios.usuarios.entity.Usuario;
import com.gestion_laboratorios.usuarios.exception.DuplicateResourceException;
import com.gestion_laboratorios.usuarios.exception.InvalidCredentialsException;
import com.gestion_laboratorios.usuarios.exception.UsuarioNotFoundException;
import com.gestion_laboratorios.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

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
                .password(passwordEncoder.encode(request.getPassword()))
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

    @Transactional
    public UsuarioResponseDto crearMedico(CreateMedicoRequestDto request) {
        UsuarioRequestDto usuarioRequest = UsuarioRequestDto.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .tipoUsuario(Usuario.TipoUsuario.MEDICO)
                .build();

        return crearUsuario(usuarioRequest);
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

    @Transactional
    public void activarUsuario(Long id) {
        log.info("Activando usuario con ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> UsuarioNotFoundException.porId(id));
        
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
        log.info("Usuario activado exitosamente con ID: {}", id);
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto loginRequest) {
        log.info("Intento de login para usuario: {}", loginRequest.getUsername());
        
        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(InvalidCredentialsException::credencialesIncorrectas);

        // Validar contraseña usando BCrypt
        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            log.warn("Contraseña incorrecta para usuario: {}", loginRequest.getUsername());
            throw InvalidCredentialsException.credencialesIncorrectas();
        }

        // Validar que el usuario esté activo
        if (!usuario.getActivo()) {
            log.warn("Usuario inactivo intentando login: {}", loginRequest.getUsername());
            throw InvalidCredentialsException.usuarioInactivo();
        }

        log.info("Login exitoso para usuario: {}", loginRequest.getUsername());
        
        // Generar JWT token
        String token = jwtTokenUtil.generateToken(
            usuario.getUsername(), 
            usuario.getTipoUsuario().name(), 
            usuario.getId()
        );
        
        return LoginResponseDto.builder()
                .mensaje("Login exitoso")
                .usuario(mapToResponseDto(usuario))
                .token(token)
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

    @Transactional
    public String iniciarRecuperacionPassword(RecoverPasswordRequestDto request) {
        log.info("Iniciando recuperación de contraseña para email: {}", request.getEmail());
        
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con email: " + request.getEmail()));
        
        if (!usuario.getActivo()) {
            throw new UsuarioNotFoundException("Usuario inactivo");
        }
        
        // Generar token único
        String token = UUID.randomUUID().toString();
        usuario.setResetPasswordToken(token);
        usuario.setResetPasswordExpires(LocalDateTime.now().plusHours(1)); // Expira en 1 hora
        
        usuarioRepository.save(usuario);
        
        log.info("Token de recuperación generado para usuario: {}", usuario.getUsername());
        
        // En un entorno real, aquí se enviaría un email con el token
        // Para desarrollo, devolvemos el token directamente
        return token;
    }

    @Transactional
    public String restablecerPassword(ResetPasswordRequestDto request) {
        log.info("Restableciendo contraseña con token: {}", request.getToken());
        
        Usuario usuario = usuarioRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new UsuarioNotFoundException("Token de recuperación inválido"));
        
        if (usuario.getResetPasswordExpires().isBefore(LocalDateTime.now())) {
            throw new UsuarioNotFoundException("Token de recuperación expirado");
        }
        
        // Actualizar contraseña
        usuario.setPassword(passwordEncoder.encode(request.getNuevaPassword()));
        usuario.setResetPasswordToken(null);
        usuario.setResetPasswordExpires(null);
        
        usuarioRepository.save(usuario);
        
        log.info("Contraseña restablecida exitosamente para usuario: {}", usuario.getUsername());
        
        return "Contraseña restablecida exitosamente";
    }
}