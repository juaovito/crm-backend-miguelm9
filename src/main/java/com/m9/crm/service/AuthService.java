package com.m9.crm.service;

import com.m9.crm.dto.LoginRequest;
import com.m9.crm.dto.LoginResponse;
import com.m9.crm.dto.RefreshTokenRequest;
import com.m9.crm.exception.RegraDeNegocioException;
import com.m9.crm.model.Usuario;
import com.m9.crm.repository.UsuarioRepository;
import com.m9.crm.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${jwt.expiration:28800000}")
    private long expirationMs;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByLogin(request.login())
                .orElseThrow(() -> new RegraDeNegocioException("Usuário ou senha incorretos"));
        if (!usuario.isAtivo())
            throw new RegraDeNegocioException("Usuário desativado. Contate o administrador.");
        if (!passwordEncoder.matches(request.senha(), usuario.getSenha()))
            throw new RegraDeNegocioException("Usuário ou senha incorretos");
        return buildResponse(usuario);
    }

    public LoginResponse refresh(RefreshTokenRequest request) {
        if (!jwtService.isValid(request.refreshToken()))
            throw new RegraDeNegocioException("Refresh token inválido ou expirado");
        String login = jwtService.extractSubject(request.refreshToken());
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
        if (!usuario.isAtivo()) throw new RegraDeNegocioException("Usuário desativado");
        return buildResponse(usuario);
    }

    private LoginResponse buildResponse(Usuario usuario) {
        Map<String, Object> claims = Map.of(
                "id",    usuario.getId(),
                "nome",  usuario.getNome()  != null ? usuario.getNome()  : usuario.getLogin(),
                "cargo", usuario.getCargo() != null ? usuario.getCargo() : "consultor"
        );
        String accessToken  = jwtService.generateAccessToken(usuario.getLogin(), claims);
        String refreshToken = jwtService.generateRefreshToken(usuario.getLogin());
        return new LoginResponse(
                usuario.getId(), usuario.getLogin(),
                usuario.getNome()  != null ? usuario.getNome()  : usuario.getLogin(),
                usuario.getCargo() != null ? usuario.getCargo() : "consultor",
                usuario.getFoto()  != null ? usuario.getFoto()  : "",
                accessToken, refreshToken, expirationMs / 1000);
    }
}
