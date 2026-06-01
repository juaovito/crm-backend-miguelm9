
package com.m9.crm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ocorrencias", indexes = {
        @Index(name = "idx_ocorrencias_cliente_id", columnList = "cliente_id"),
        @Index(name = "idx_ocorrencias_criado_em",  columnList = "criadoEm")
})
@EntityListeners(AuditingEntityListener.class)
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String informacoes;

    @Column(length = 50)
    private String estagio;

    private LocalDateTime agendamento;

    @Column(length = 100)
    private String usuario;

    /** "Em andamento" | "Concluído" | "Cancelado" */
    @Column(length = 50)
    private String status = "Em andamento";

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;
}
