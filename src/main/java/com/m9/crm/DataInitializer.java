package com.m9.crm;

import com.m9.crm.model.Usuario;
import com.m9.crm.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

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
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setCargo("admin");
            admin.setAtivo(true);
            usuarioRepository.save(admin);
            System.out.println("Admin criado com sucesso!");
        }
    }
}
