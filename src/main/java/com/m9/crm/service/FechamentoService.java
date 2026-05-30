package com.m9.crm.service;

import com.m9.crm.dto.FechamentoRequest;
import com.m9.crm.exception.RecursoNaoEncontradoException;
import com.m9.crm.model.Fechamento;
import com.m9.crm.repository.ClienteRepository;
import com.m9.crm.repository.FechamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FechamentoService {

    private final FechamentoRepository fechamentoRepository;
    private final ClienteRepository    clienteRepository;

    public FechamentoService(FechamentoRepository fechamentoRepository,
                             ClienteRepository    clienteRepository) {
        this.fechamentoRepository = fechamentoRepository;
        this.clienteRepository    = clienteRepository;
    }

    @Transactional
    public Fechamento criar(FechamentoRequest req) {
        clienteRepository.findById(req.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado: " + req.clienteId()));

        Fechamento f = new Fechamento();
        f.setClienteId(req.clienteId());
        f.setValorVenda(req.valorVenda());
        f.setFisico(req.fisico());
        f.setVisita(req.visita());
        f.setLogonCliente(req.logonCliente());
        f.setDataVencimento(req.dataVencimento());
        f.setDiaFechamento(req.diaFechamento());
        f.setCondicaoEspecial(req.condicaoEspecial());
        f.setSituacaoVenda(req.situacaoVenda());
        f.setSituacaoData(req.situacaoData());
        f.setComissao(req.comissao());

        return fechamentoRepository.save(f);
    }

    @Transactional(readOnly = true)
    public List<Fechamento> listarPorCliente(Long clienteId) {
        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado: " + clienteId));
        return fechamentoRepository.findByClienteIdOrderByCriadoEmDesc(clienteId);
    }

    @Transactional(readOnly = true)
    public Fechamento buscarPorId(Long id) {
        return fechamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Fechamento não encontrado: " + id));
    }

    @Transactional
    public void deletar(Long id) {
        fechamentoRepository.delete(buscarPorId(id));
    }
}
