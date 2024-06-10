package com.kleben.contas.model;

import com.kleben.contas.enums.SituacaoEnum;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conta", schema = "public")
public class Conta {

    @Id
    @SequenceGenerator(name = "conta_id_seq",
            sequenceName = "conta_id_seq", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_id_seq")
    private Long id;

    @CsvBindByName
    @Column(name = "descricao", length = 255, nullable = false)
    private String descricao;

    @CsvBindByName
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @CsvBindByName
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @CsvBindByName
    @CsvDate("yyyy-MM-dd")
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @CsvBindByName
    @CsvDate("yyyy-MM-dd")
    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @CsvBindByName
    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @CsvBindByName
    @Column(name = "situacao", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "A nova situação não pode ser nula")
    private SituacaoEnum situacao;
}
