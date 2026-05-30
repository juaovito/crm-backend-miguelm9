package com.m9.crm.repository;

import com.m9.crm.model.Fechamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FechamentoRepository extends JpaRepository<Fechamento, Long> {
    List<Fechamento> findByClienteIdOrderByCriadoEmDesc(Long clienteId);
}
