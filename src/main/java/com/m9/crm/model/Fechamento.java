package com.m9.crm.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "fechamentos", indexes = {
    @Index(name = "idx_fechamentos_cliente_id", columnList = "cliente_id")
})
@EntityListeners(AuditingEntityListener.class)
public class Fechamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(precision = 15, scale = 2)
    private BigDecimal valorVenda;

    private Boolean fisico;
    private Boolean visita;

    @Column(length = 100)
    private String logonCliente;

    private LocalDate dataVencimento;

    @Column(length = 2)
    private String diaFechamento;

    @Column(length = 200)
    private String condicaoEspecial;

    @Column(length = 50)
    private String situacaoVenda;

    private LocalDate situacaoData;

    @Column(precision = 5, scale = 2)
    private BigDecimal comissao;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;
}
