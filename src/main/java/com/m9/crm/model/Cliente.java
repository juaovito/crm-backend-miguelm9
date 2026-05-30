package com.m9.crm.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "clientes", indexes = {
        @Index(name = "idx_clientes_status",     columnList = "status"),
        @Index(name = "idx_clientes_consultor",  columnList = "consultor"),
        @Index(name = "idx_clientes_criado_por", columnList = "criadoPor"),
        @Index(name = "idx_clientes_empresa",    columnList = "empresa")
})
@EntityListeners(AuditingEntityListener.class)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)  private String origem;
    @Column(length = 50)  private String contrato;
    @Column(length = 150) private String empresa;
    @Column(length = 18)  private String cnpj;
    @Column(length = 14)  private String cpf;
    @Column(length = 30)  private String inscricaoEstadual;
    @Column(length = 100) private String nomeContato;
    @Column(length = 80)  private String cargo;
    @Column(length = 200) private String endereco;
    @Column(length = 100) private String bairro;
    @Column(length = 9)   private String cep;
    @Column(length = 80)  private String cidade;
    @Column(length = 2)   private String estado;
    @Column(length = 150) private String email;
    @Column(length = 20)  private String telefone;
    @Column(length = 20)  private String telefone2;
    @Column(length = 100) private String consultor;

    @Column(precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(length = 50)  private String status;
    private LocalDate dataEntrada;
    private LocalDate prorrogacao;
    @Column(length = 100) private String responsavel;
    private Long criadoPor;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(columnDefinition = "TEXT")
    private String motivoPerda;

    @Column(length = 100)
    private String responsavelPerda;

    private LocalDateTime perdaRegistradaEm;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime atualizadoEm;
}
