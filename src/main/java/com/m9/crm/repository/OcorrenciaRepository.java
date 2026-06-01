package com.m9.crm.repository;

import com.m9.crm.model.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {

    List<Ocorrencia> findByClienteIdOrderByCriadoEmDesc(Long clienteId);
}
