package com.gestion_laboratorios.usuarios.controller;

import com.gestion_laboratorios.usuarios.dto.*;
import com.gestion_laboratorios.usuarios.entity.Usuario;
import com.gestion_laboratorios.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> crearUsuario(@Valid @RequestBody UsuarioRequestDto request) {
        UsuarioResponseDto usuario = usuarioService.crearUsuario(request);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> obtenerTodosLosUsuarios() {
        List<UsuarioResponseDto> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioResponseDto>> obtenerUsuariosActivos() {
        List<UsuarioResponseDto> usuarios = usuarioService.obtenerUsuariosActivos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioResponseDto usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioPorUsername(@PathVariable String username) {
        UsuarioResponseDto usuario = usuarioService.obtenerUsuarioPorUsername(username);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/tipo/{tipoUsuario}")
    public ResponseEntity<List<UsuarioResponseDto>> obtenerUsuariosPorTipo(
            @PathVariable Usuario.TipoUsuario tipoUsuario) {
        List<UsuarioResponseDto> usuarios = usuarioService.obtenerUsuariosPorTipo(tipoUsuario);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponseDto>> buscarUsuarios(@RequestParam String q) {
        List<UsuarioResponseDto> usuarios = usuarioService.buscarUsuarios(q);
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> actualizarUsuario(
            @PathVariable Long id, 
            @Valid @RequestBody UsuarioUpdateDto updateDto) {
        UsuarioResponseDto usuario = usuarioService.actualizarUsuario(id, updateDto);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activarUsuario(@PathVariable Long id) {
        usuarioService.activarUsuario(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = usuarioService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDto> registrarUsuario(@Valid @RequestBody UsuarioRequestDto request) {
        UsuarioResponseDto usuario = usuarioService.crearUsuario(request);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @PostMapping("/recover-password")
    public ResponseEntity<Map<String, String>> iniciarRecuperacionPassword(
            @Valid @RequestBody RecoverPasswordRequestDto request) {
        String token = usuarioService.iniciarRecuperacionPassword(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Si el email existe, se ha enviado un enlace de recuperación");
        response.put("token", token); // Solo para desarrollo, remover en producción
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> restablecerPassword(
            @Valid @RequestBody ResetPasswordRequestDto request) {
        String mensaje = usuarioService.restablecerPassword(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        
        return ResponseEntity.ok(response);
    }
}