package com.m9.crm.repository;

import com.m9.crm.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Page<Cliente> findByConsultor(String consultor, Pageable pageable);
    Page<Cliente> findByCriadoPor(Long criadoPor, Pageable pageable);
    Page<Cliente> findByStatus(String status, Pageable pageable);

    @Query("""
            SELECT c FROM Cliente c
            WHERE (:termo IS NULL OR :termo = ''
                   OR LOWER(c.empresa)     LIKE LOWER(CONCAT('%', :termo, '%'))
                   OR LOWER(c.nomeContato) LIKE LOWER(CONCAT('%', :termo, '%'))
                   OR LOWER(c.email)       LIKE LOWER(CONCAT('%', :termo, '%'))
                   OR c.cnpj              LIKE CONCAT('%', :termo, '%')
                   OR c.cpf               LIKE CONCAT('%', :termo, '%'))
              AND (:status IS NULL OR :status = '' OR c.status = :status)
              AND (:consultor IS NULL OR :consultor = '' OR c.consultor = :consultor)
            """)
    Page<Cliente> buscar(@Param("termo") String termo,
                         @Param("status") String status,
                         @Param("consultor") String consultor,
                         Pageable pageable);

    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(c.valor), 0) FROM Cliente c WHERE c.status = :status")
    BigDecimal somarValorPorStatus(@Param("status") String status);

    @Query("SELECT COALESCE(SUM(c.valor), 0) FROM Cliente c")
    BigDecimal somarValorTotal();

    @Query("SELECT c.status, COUNT(c), COALESCE(SUM(c.valor), 0) FROM Cliente c GROUP BY c.status")
    List<Object[]> resumoPorStatus();

    @Query("""
            SELECT c.consultor, COUNT(c), COALESCE(SUM(c.valor), 0)
            FROM Cliente c GROUP BY c.consultor ORDER BY COUNT(c) DESC
            """)
    List<Object[]> resumoPorConsultor();
}
