CREATE TABLE conta (
    id SERIAL PRIMARY KEY,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
    data_atualizacao TIMESTAMP,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor DECIMAL(10, 2) NOT NULL,
    descricao VARCHAR(255),
    situacao VARCHAR(50)
);