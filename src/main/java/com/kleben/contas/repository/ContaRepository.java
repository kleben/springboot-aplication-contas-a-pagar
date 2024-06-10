package com.kleben.contas.repository;

import com.kleben.contas.controller.dto.PeriodoDTO;
import com.kleben.contas.enums.SituacaoEnum;
import com.kleben.contas.model.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    Page<Conta> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);

    Page<Conta> findByDataVencimento(LocalDate dataVencimento, Pageable pageable);

    @Query("SELECT c FROM Conta c WHERE LOWER(c.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')) AND c.dataVencimento = :dataVencimento AND c.situacao <> 'PAGO'")
    Page<Conta> findByDescricaoAndDataVencimento(
            @Param("descricao") String descricao,
            @Param("dataVencimento") LocalDate dataVencimento,
            Pageable pageable);

    @Query("select sum(c.valor) from Conta c where c.dataPagamento between :dataInicial and :dataFinal")
    BigDecimal totalPorPeriodo(@Param("dataInicial") LocalDate dataInicio,
                               @Param("dataFinal") LocalDate dataFim);

}