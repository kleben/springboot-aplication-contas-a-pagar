package com.kleben.contas.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PeriodoDTO {
    private LocalDate dataInicial;
    private LocalDate dataFinal;
}
