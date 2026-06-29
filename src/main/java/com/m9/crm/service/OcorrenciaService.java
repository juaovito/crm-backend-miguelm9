package com.m9.crm.service;

import com.m9.crm.dto.OcorrenciaRequest;
import com.m9.crm.dto.OcorrenciaAtrasadaResponse;
import com.m9.crm.dto.OcorrenciaStatusRequest;
import com.m9.crm.exception.RecursoNaoEncontradoException;
import com.m9.crm.exception.RegraDeNegocioException;
import com.m9.crm.model.Cliente;
import com.m9.crm.model.Ocorrencia;
import com.m9.crm.model.Usuario;
import com.m9.crm.repository.ClienteRepository;
import com.m9.crm.repository.OcorrenciaRepository;
import com.m9.crm.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OcorrenciaService {

    private static final List<String> STATUS_VALIDOS =
            List.of("Em andamento", "Concluído", "Cancelado");

    private final OcorrenciaRepository ocorrenciaRepository;
    private final ClienteRepository    clienteRepository;
    private final UsuarioRepository    usuarioRepository;

    public OcorrenciaService(OcorrenciaRepository ocorrenciaRepository,
                             ClienteRepository clienteRepository,
                             UsuarioRepository usuarioRepository) {
        this.ocorrenciaRepository = ocorrenciaRepository;
        this.clienteRepository    = clienteRepository;
        this.usuarioRepository    = usuarioRepository;
    }

    public List<OcorrenciaAtrasadaResponse> listarAtrasadas(UserDetails principal) {
        boolean isAdminOuGerente = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                            || a.getAuthority().equals("ROLE_GERENTE"));

        Long criadoPor = null;
        if (!isAdminOuGerente) {
            Usuario usuarioLogado = usuarioRepository.findByLogin(principal.getUsername())
                    .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));
            criadoPor = usuarioLogado.getId();
        }

        return ocorrenciaRepository
                .findAtrasadas(LocalDateTime.now(), criadoPor)
                .stream()
                .map(OcorrenciaAtrasadaResponse::de)
                .toList();
    }

    public List<Ocorrencia> listarPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId))
            throw new RecursoNaoEncontradoException("Cliente não encontrado: " + clienteId);
        return ocorrenciaRepository.findByClienteIdOrderByCriadoEmDesc(clienteId);
    }

    @Transactional
    public Ocorrencia criar(Long clienteId, OcorrenciaRequest request, UserDetails principal) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + clienteId));

        verificarPermissao(cliente, principal);

        Ocorrencia oc = new Ocorrencia();
        oc.setCliente(cliente);
        oc.setInformacoes(request.informacoes());
        oc.setEstagio(request.estagio());
        oc.setAgendamento(request.agendamento());
        oc.setUsuario(request.usuario());
        oc.setStatus("Em andamento");

        return ocorrenciaRepository.save(oc);
    }

    @Transactional
    public Ocorrencia atualizarStatus(Long clienteId, Long ocorrenciaId,
                                      OcorrenciaStatusRequest request, UserDetails principal) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + clienteId));

        verificarPermissao(cliente, principal);

        Ocorrencia oc = ocorrenciaRepository.findById(ocorrenciaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ocorrência não encontrada: " + ocorrenciaId));

        if (!oc.getCliente().getId().equals(clienteId))
            throw new RegraDeNegocioException("Ocorrência não pertence ao cliente informado.");

        if (!STATUS_VALIDOS.contains(request.status()))
            throw new RegraDeNegocioException(
                    "Status inválido. Use: " + String.join(", ", STATUS_VALIDOS));

        oc.setStatus(request.status());
        return ocorrenciaRepository.save(oc);
    }

    @Transactional
    public void deletar(Long clienteId, Long ocorrenciaId, UserDetails principal) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + clienteId));

        verificarPermissao(cliente, principal);

        Ocorrencia oc = ocorrenciaRepository.findById(ocorrenciaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ocorrência não encontrada: " + ocorrenciaId));

        if (!oc.getCliente().getId().equals(clienteId))
            throw new RegraDeNegocioException("Ocorrência não pertence ao cliente informado.");

        ocorrenciaRepository.delete(oc);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Admin e Gerente podem operar em qualquer cliente.
     * Consultor só pode operar nos clientes que ele criou (criadoPor == seu id).
     */
    private void verificarPermissao(Cliente cliente, UserDetails principal) {
        boolean isAdminOuGerente = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                            || a.getAuthority().equals("ROLE_GERENTE"));

        if (isAdminOuGerente) return;

        Usuario usuarioLogado = usuarioRepository.findByLogin(principal.getUsername())
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));

        if (!usuarioLogado.getId().equals(cliente.getCriadoPor())) {
            throw new RegraDeNegocioException(
                    "Você não tem permissão para modificar ocorrências deste cliente.");
        }
    }
}
