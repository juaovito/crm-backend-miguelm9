package com.m9.crm.repository;

import com.m9.crm.model.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {

    List<Ocorrencia> findByClienteIdOrderByCriadoEmDesc(Long clienteId);

    /**
     * Retorna ocorrências com agendamento no passado e status "Em andamento".
     * Se o usuário for consultor, filtra apenas os clientes criados por ele.
     * Admin/Gerente passam criadoPor = null para ver todos.
     */
    @Query("""
        SELECT o FROM Ocorrencia o
        JOIN FETCH o.cliente c
        WHERE o.agendamento < :agora
          AND o.status = 'Em andamento'
          AND (:criadoPor IS NULL OR c.criadoPor = :criadoPor)
        ORDER BY o.agendamento ASC
    """)
    List<Ocorrencia> findAtrasadas(
            @Param("agora") LocalDateTime agora,
            @Param("criadoPor") Long criadoPor
    );
}
