package com.m9.crm.service;

import com.m9.crm.model.Cliente;
import com.m9.crm.model.Usuario;
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

    private static final int PIPELINE_LIMITE_POR_STATUS = 10;

    private final ClienteRepository clienteRepository;

    public DashboardService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // ── Resumo geral (cards de topo) ─────────────────────────────────────────

    /**
     * Quando o usuário é admin/gerente retorna dados globais.
     * Quando é consultor comum, filtra apenas pelos clientes que ele criou.
     */
    public Map<String, Object> resumoGeral(Usuario usuarioLogado) {
        boolean isAdmin = isAdminOuGerente(usuarioLogado);

        long totalClientes;
        BigDecimal valorTotal;
        List<Map<String, Object>> porStatus     = new ArrayList<>();
        List<Map<String, Object>> porConsultor  = new ArrayList<>();

        if (isAdmin) {
            totalClientes = clienteRepository.count();
            valorTotal    = clienteRepository.somarValorTotal();

            for (Object[] row : clienteRepository.resumoPorStatus()) {
                porStatus.add(montarItemStatus(row));
            }
            for (Object[] row : clienteRepository.resumoPorConsultor()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("consultor", row[0]);
                item.put("total",     row[1]);
                item.put("valor",     row[2]);
                porConsultor.add(item);
            }
        } else {
            Long dono = usuarioLogado.getId();
            totalClientes = clienteRepository.countByCriadoPor(dono);
            valorTotal    = clienteRepository.somarValorTotalPorDono(dono);

            for (Object[] row : clienteRepository.resumoPorStatusEDono(dono)) {
                porStatus.add(montarItemStatus(row));
            }
            // consultor individual não vê ranking de outros consultores
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalClientes", totalClientes);
        result.put("valorTotal",    valorTotal);
        result.put("porStatus",     porStatus);
        result.put("porConsultor",  porConsultor);
        return result;
    }

    // ── Pipeline: contagens + últimos N cards por coluna ─────────────────────

    public Map<String, Object> pipeline(Usuario usuarioLogado) {
        List<String> statusPipeline = List.of("Novo", "Contato", "Proposta", "Negociação", "Fechado");
        PageRequest pagina          = PageRequest.of(0, PIPELINE_LIMITE_POR_STATUS);
        boolean isAdmin             = isAdminOuGerente(usuarioLogado);
        Long dono                   = usuarioLogado.getId();

        long retornosHoje = isAdmin
                ? clienteRepository.countRetornosHoje(LocalDate.now())
                : clienteRepository.countRetornosHojeEDono(LocalDate.now(), dono);

        List<Map<String, Object>> colunas = new ArrayList<>();
        for (String status : statusPipeline) {
            long total;
            List<Cliente> recentes;

            if (isAdmin) {
                total    = clienteRepository.countByStatus(status);
                recentes = clienteRepository.findTopByStatusOrderByCriadoEmDesc(status, pagina);
            } else {
                total    = clienteRepository.countByStatusAndCriadoPor(status, dono);
                recentes = clienteRepository.findTopByStatusAndCriadoPorOrderByCriadoEmDesc(status, dono, pagina);
            }

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

    // ── Helpers privados ─────────────────────────────────────────────────────

    private boolean isAdminOuGerente(Usuario u) {
        if (u == null || u.getCargo() == null) return false;
        String cargo = u.getCargo().toLowerCase().trim();
        return cargo.equals("admin") || cargo.equals("gerente");
    }

    private Map<String, Object> montarItemStatus(Object[] row) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("status", row[0]);
        item.put("total",  row[1]);
        item.put("valor",  row[2]);
        return item;
    }
}
