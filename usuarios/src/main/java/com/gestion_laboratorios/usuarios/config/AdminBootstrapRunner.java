package com.gestion_laboratorios.usuarios.config;

import com.gestion_laboratorios.usuarios.entity.Usuario;
import com.gestion_laboratorios.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminBootstrapRunner implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.admin.enabled:true}")
    private boolean enabled;

    @Value("${app.bootstrap.admin.username:admin}")
    private String adminUsername;

    @Value("${app.bootstrap.admin.password:admin123}")
    private String adminPassword;

    @Value("${app.bootstrap.admin.email:admin@local}")
    private String adminEmail;

    @Value("${app.bootstrap.admin.nombre:Administrador}")
    private String adminNombre;

    @Value("${app.bootstrap.admin.apellido:Sistema}")
    private String adminApellido;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!enabled) {
            return;
        }

        if (usuarioRepository.existsByTipoUsuario(Usuario.TipoUsuario.ADMIN)) {
            return;
        }

        if (usuarioRepository.existsByUsername(adminUsername) || usuarioRepository.existsByEmail(adminEmail)) {
            log.warn("Bootstrap admin habilitado pero username/email ya existen. No se crea admin por defecto.");
            return;
        }

        Usuario admin = Usuario.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .email(adminEmail)
                .nombre(adminNombre)
                .apellido(adminApellido)
                .tipoUsuario(Usuario.TipoUsuario.ADMIN)
                .activo(true)
                .build();

        usuarioRepository.save(admin);
        log.warn("Usuario ADMIN bootstrap creado: username='{}' email='{}'. Cambia la contraseña inmediatamente.", adminUsername, adminEmail);
    }
}