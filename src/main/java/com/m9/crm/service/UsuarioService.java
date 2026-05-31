package com.m9.crm.service;

import com.m9.crm.dto.ConsultorDTO;
import com.m9.crm.dto.UsuarioRequest;
import com.m9.crm.dto.UsuarioResponse;
import com.m9.crm.exception.RecursoNaoEncontradoException;
import com.m9.crm.exception.RegraDeNegocioException;
import com.m9.crm.model.Usuario;
import com.m9.crm.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponse> listarAtivos() {
        return usuarioRepository.findByAtivoTrue().stream().map(UsuarioResponse::from).toList();
    }

    /** Retorna apenas id + nome dos usuários ativos — sem dados sensíveis. */
    public List<ConsultorDTO> listarConsultores() {
        return usuarioRepository.findByAtivoTrue().stream()
                .map(u -> new ConsultorDTO(u.getId(), u.getNome() != null ? u.getNome() : u.getLogin()))
                .toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        return UsuarioResponse.from(findOrThrow(id));
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest request) {
        if (usuarioRepository.existsByLogin(request.login()))
            throw new RegraDeNegocioException("Login já está em uso: " + request.login());
        if (request.senha() == null || request.senha().isBlank())
            throw new RegraDeNegocioException("Senha é obrigatória na criação do usuário");
        Usuario u = new Usuario();
        u.setLogin(request.login()); u.setNome(request.nome());
        u.setSenha(passwordEncoder.encode(request.senha()));
        u.setCargo(request.cargo()); u.setFoto(request.foto()); u.setAtivo(true);
        return UsuarioResponse.from(usuarioRepository.save(u));
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario u = findOrThrow(id);
        if (!u.getLogin().equals(request.login()) && usuarioRepository.existsByLogin(request.login()))
            throw new RegraDeNegocioException("Login já está em uso: " + request.login());
        u.setLogin(request.login()); u.setNome(request.nome());
        u.setCargo(request.cargo()); u.setFoto(request.foto());
        if (request.senha() != null && !request.senha().isBlank())
            u.setSenha(passwordEncoder.encode(request.senha()));
        return UsuarioResponse.from(usuarioRepository.save(u));
    }

    @Transactional
    public void desativar(Long id) {
        Usuario u = findOrThrow(id);
        u.setAtivo(false);
        usuarioRepository.save(u);
    }

    @Transactional
    public UsuarioResponse reativar(Long id) {
        Usuario u = findOrThrow(id);
        u.setAtivo(true);
        return UsuarioResponse.from(usuarioRepository.save(u));
    }

    private Usuario findOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
    }
}
