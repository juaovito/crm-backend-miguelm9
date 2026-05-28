package com.m9.crm;

import com.m9.crm.model.Usuario;
import com.m9.crm.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_PASSWORD:admin123}")
    private String adminPassword;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByLogin("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setNome("Administrador");
            admin.setSenha(passwordEncoder.encode(adminPassword));
            admin.setCargo("admin");
            admin.setAtivo(true);
            usuarioRepository.save(admin);
            System.out.println("Admin criado com sucesso!");
        }
    }
}
