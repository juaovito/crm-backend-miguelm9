package com.m9.crm.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FechamentoRequest(

        @NotNull(message = "clienteId é obrigatório")
        Long clienteId,

        @DecimalMin(value = "0.0", message = "valorVenda não pode ser negativo")
        BigDecimal valorVenda,

        Boolean fisico,
        Boolean visita,

        @Size(max = 100)  String logonCliente,
        LocalDate dataVencimento,
        @Size(max = 2)    String diaFechamento,
        @Size(max = 200)  String condicaoEspecial,
        @Size(max = 50)   String situacaoVenda,
        LocalDate situacaoData,

        @DecimalMin(value = "0.0", message = "comissao não pode ser negativa")
        BigDecimal comissao
) {}
