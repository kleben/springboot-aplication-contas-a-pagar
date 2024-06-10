INSERT INTO conta (data_criacao, data_atualizacao, data_vencimento, data_pagamento, valor, descricao, situacao)
SELECT
            CURRENT_TIMESTAMP - (RANDOM() * INTERVAL '365 days'), -- data_criacao aleatória no último ano
            NULL, -- data_atualizacao
            CURRENT_DATE + (RANDOM() * INTERVAL '30 days'), -- data_vencimento aleatória nos próximos 30 dias
            CASE WHEN RANDOM() > 0.5 THEN CURRENT_DATE - (RANDOM() * INTERVAL '30 days') ELSE NULL END, -- data_pagamento aleatória ou NULL
            ROUND((RANDOM() * 1000)::NUMERIC, 2), -- valor entre 0 e 1000 com 2 casas decimais
            CONCAT('Descrição ', g), -- descrição fictícia
            CASE WHEN RANDOM() > 0.5 THEN 'PAGO' ELSE 'PENDENTE' END -- situação aleatória entre 'Pago' e 'Pendente'
FROM generate_series(1, 100) AS g;
