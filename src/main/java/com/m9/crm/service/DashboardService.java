package com.m9.crm.service;

import com.m9.crm.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final ClienteRepository clienteRepository;

    public DashboardService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Map<String, Object> resumoGeral() {
        long totalClientes = clienteRepository.count();
        BigDecimal valorTotal = clienteRepository.somarValorTotal();

        List<Map<String, Object>> porStatus = new ArrayList<>();
        for (Object[] row : clienteRepository.resumoPorStatus()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("status", row[0]); item.put("total", row[1]); item.put("valor", row[2]);
            porStatus.add(item);
        }

        List<Map<String, Object>> porConsultor = new ArrayList<>();
        for (Object[] row : clienteRepository.resumoPorConsultor()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("consultor", row[0]); item.put("total", row[1]); item.put("valor", row[2]);
            porConsultor.add(item);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalClientes", totalClientes);
        result.put("valorTotal", valorTotal);
        result.put("porStatus", porStatus);
        result.put("porConsultor", porConsultor);
        return result;
    }
}
