package com.m9.crm.dto;

import com.m9.crm.model.Ocorrencia;

import java.time.LocalDateTime;

public record OcorrenciaAtrasadaResponse(
        Long ocorrenciaId,
        Long clienteId,
        String nomeCliente,
        String empresa,
        String informacoes,
        String estagio,
        LocalDateTime agendamento,
        String consultor
) {
    public static OcorrenciaAtrasadaResponse de(Ocorrencia oc) {
        return new OcorrenciaAtrasadaResponse(
                oc.getId(),
                oc.getCliente().getId(),
                oc.getCliente().getNomeContato(),
                oc.getCliente().getEmpresa(),
                oc.getInformacoes(),
                oc.getEstagio(),
                oc.getAgendamento(),
                oc.getCliente().getConsultor()
        );
    }
}
