package com.m9.crm.service;

import com.m9.crm.dto.ClienteRequest;
import com.m9.crm.exception.RecursoNaoEncontradoException;
import com.m9.crm.model.Cliente;
import com.m9.crm.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return clienteRepository.save(toEntity(new Cliente(), request, usuarioId));
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarPorId(id);
        Long criadoPor = cliente.getCriadoPor(); // preserva
        toEntity(cliente, request, criadoPor);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id))
            throw new RecursoNaoEncontradoException("Cliente não encontrado: " + id);
        clienteRepository.deleteById(id);
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
