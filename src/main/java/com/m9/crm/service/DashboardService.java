package com.m9.crm.service;

import com.m9.crm.model.Cliente;
import com.m9.crm.repository.ClienteRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    // Quantos cards mostrar por coluna no pipeline
    private static final int PIPELINE_LIMITE_POR_STATUS = 10;

    private final ClienteRepository clienteRepository;

    public DashboardService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // ── Resumo geral (cards de topo + gráficos) ──────────────────────────────

    public Map<String, Object> resumoGeral() {
        long totalClientes = clienteRepository.count();
        BigDecimal valorTotal = clienteRepository.somarValorTotal();

        List<Map<String, Object>> porStatus = new ArrayList<>();
        for (Object[] row : clienteRepository.resumoPorStatus()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("status", row[0]);
            item.put("total",  row[1]);
            item.put("valor",  row[2]);
            porStatus.add(item);
        }

        List<Map<String, Object>> porConsultor = new ArrayList<>();
        for (Object[] row : clienteRepository.resumoPorConsultor()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("consultor", row[0]);
            item.put("total",     row[1]);
            item.put("valor",     row[2]);
            porConsultor.add(item);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalClientes", totalClientes);
        result.put("valorTotal",    valorTotal);
        result.put("porStatus",     porStatus);
        result.put("porConsultor",  porConsultor);
        return result;
    }

    // ── Pipeline: contagens + últimos N cards por coluna ─────────────────────

    public Map<String, Object> pipeline() {
        List<String> statusPipeline = List.of("Novo", "Contato", "Proposta", "Negociação", "Fechado");
        PageRequest pagina = PageRequest.of(0, PIPELINE_LIMITE_POR_STATUS);

        // Contagem de retornos hoje (prorrogação = data de hoje)
        long retornosHoje = clienteRepository.countRetornosHoje(LocalDate.now());

        List<Map<String, Object>> colunas = new ArrayList<>();
        for (String status : statusPipeline) {
            long total = clienteRepository.countByStatus(status);
            List<Cliente> recentes = clienteRepository.findTopByStatusOrderByCriadoEmDesc(status, pagina);

            List<Map<String, Object>> cards = recentes.stream()
                    .map(c -> {
                        Map<String, Object> card = new LinkedHashMap<>();
                        card.put("id",          c.getId());
                        card.put("empresa",     c.getEmpresa());
                        card.put("nomeContato", c.getNomeContato());
                        return card;
                    })
                    .toList();

            Map<String, Object> coluna = new LinkedHashMap<>();
            coluna.put("status", status);
            coluna.put("total",  total);
            coluna.put("cards",  cards);
            colunas.add(coluna);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("retornosHoje", retornosHoje);
        result.put("colunas",      colunas);
        return result;
    }
}
