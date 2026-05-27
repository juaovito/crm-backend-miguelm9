package com.m9.crm.security;

import com.m9.crm.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));

        String[] roles = switch (usuario.getCargo() == null ? "" : usuario.getCargo().toLowerCase().trim()) {
            case "admin"   -> new String[]{"ADMIN", "USER"};
            case "gerente" -> new String[]{"GERENTE", "USER"};
            default        -> new String[]{"USER"};
        };

        return User.withUsername(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }
}
