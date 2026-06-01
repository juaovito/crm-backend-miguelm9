package com.m9.crm.service;

import com.m9.crm.dto.OcorrenciaRequest;
import com.m9.crm.dto.OcorrenciaStatusRequest;
import com.m9.crm.exception.RecursoNaoEncontradoException;
import com.m9.crm.exception.RegraDeNegocioException;
import com.m9.crm.model.Cliente;
import com.m9.crm.model.Ocorrencia;
import com.m9.crm.repository.ClienteRepository;
import com.m9.crm.repository.OcorrenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OcorrenciaService {

    private static final List<String> STATUS_VALIDOS =
            List.of("Em andamento", "Concluído", "Cancelado");

    private final OcorrenciaRepository ocorrenciaRepository;
    private final ClienteRepository    clienteRepository;

    public OcorrenciaService(OcorrenciaRepository ocorrenciaRepository,
                             ClienteRepository clienteRepository) {
        this.ocorrenciaRepository = ocorrenciaRepository;
        this.clienteRepository    = clienteRepository;
    }

    public List<Ocorrencia> listarPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId))
            throw new RecursoNaoEncontradoException("Cliente não encontrado: " + clienteId);
        return ocorrenciaRepository.findByClienteIdOrderByCriadoEmDesc(clienteId);
    }

    @Transactional
    public Ocorrencia criar(Long clienteId, OcorrenciaRequest request) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + clienteId));

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
                                      OcorrenciaStatusRequest request) {
        if (!clienteRepository.existsById(clienteId))
            throw new RecursoNaoEncontradoException("Cliente não encontrado: " + clienteId);

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
}
