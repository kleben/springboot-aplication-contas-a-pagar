package com.kleben.contas.enums;

public enum SituacaoEnum {
    PENDENTE,
    PAGO,
    CANCELADO,
    APROVADO;

    public static SituacaoEnum fromString(String situacao) {
        for (SituacaoEnum s : SituacaoEnum.values()) {
            if (s.name().equalsIgnoreCase(situacao)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No enum constant for string: " + situacao);
    }
}
