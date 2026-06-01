package com.m9.crm.service;

import com.m9.crm.dto.ClienteRequest;
import com.m9.crm.dto.MarcarPerdidoRequest;
import com.m9.crm.exception.RecursoNaoEncontradoException;
import com.m9.crm.exception.RegraDeNegocioException;
import com.m9.crm.model.Cliente;
import com.m9.crm.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Page<Cliente> buscar(String termo, String status, String consultor, Pageable pageable) {
        return clienteRepository.buscar(termo, status, consultor, pageable);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + id));
    }

    @Transactional
    public Cliente criar(ClienteRequest request, Long usuarioId) {
        validarDuplicidade(request.cnpj(), request.email(), null);
        return clienteRepository.save(toEntity(new Cliente(), request, usuarioId));
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarPorId(id);
        validarDuplicidade(request.cnpj(), request.email(), id);
        toEntity(cliente, request, cliente.getCriadoPor());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente marcarPerdido(Long id, MarcarPerdidoRequest request) {
        Cliente cliente = buscarPorId(id);
        cliente.setStatus("Perdido");
        cliente.setMotivoPerda(request.motivo());
        cliente.setResponsavelPerda(request.responsavel());
        cliente.setPerdaRegistradaEm(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id))
            throw new RecursoNaoEncontradoException("Cliente não encontrado: " + id);
        clienteRepository.deleteById(id);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Verifica duplicidade de CNPJ e e-mail.
     * @param idAtual null na criação; id do registro em edição na atualização (para ignorar o próprio).
     */
    private void validarDuplicidade(String cnpj, String email, Long idAtual) {
        Long excluirId = idAtual != null ? idAtual : 0L;

        if (cnpj != null && !cnpj.isBlank()
                && clienteRepository.existsByCnpjAndIdNot(cnpj, excluirId)) {
            throw new RegraDeNegocioException(
                    "Já existe um cliente cadastrado com o CNPJ " + cnpj + ".");
        }

        if (email != null && !email.isBlank()
                && clienteRepository.existsByEmailAndIdNot(email, excluirId)) {
            throw new RegraDeNegocioException(
                    "Já existe um cliente cadastrado com o e-mail " + email + ".");
        }
    }

    private Cliente toEntity(Cliente c, ClienteRequest r, Long criadoPor) {
        c.setOrigem(r.origem()); c.setContrato(r.contrato()); c.setEmpresa(r.empresa());
        c.setCnpj(r.cnpj()); c.setCpf(r.cpf()); c.setInscricaoEstadual(r.inscricaoEstadual());
        c.setNomeContato(r.nomeContato());
        c.setCargo(r.cargo()); c.setEndereco(r.endereco()); c.setBairro(r.bairro());
        c.setCep(r.cep()); c.setCidade(r.cidade()); c.setEstado(r.estado());
        c.setEmail(r.email()); c.setTelefone(r.telefone()); c.setTelefone2(r.telefone2());
        c.setConsultor(r.consultor()); c.setValor(r.valor()); c.setStatus(r.status());
        c.setDataEntrada(r.dataEntrada()); c.setProrrogacao(r.prorrogacao());
        c.setResponsavel(r.responsavel()); c.setObservacoes(r.observacoes());
        c.setCriadoPor(criadoPor);
        return c;
    }
}
