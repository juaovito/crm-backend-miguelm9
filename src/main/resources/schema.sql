CREATE TABLE IF NOT EXISTS usuarios (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    login          VARCHAR(100) NOT NULL,
    nome           VARCHAR(100) NOT NULL,
    senha          VARCHAR(255) NOT NULL COMMENT 'Hash BCrypt',
    cargo          VARCHAR(50)  NULL,
    foto           VARCHAR(500) NULL,
    ativo          TINYINT(1)   NOT NULL DEFAULT 1,
    criado_em      DATETIME(6)  NOT NULL,
    atualizado_em  DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuarios_login (login)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS clientes (
    id             BIGINT        NOT NULL AUTO_INCREMENT,
    origem         VARCHAR(50)   NULL,
    contrato       VARCHAR(50)   NULL,
    empresa        VARCHAR(150)  NULL,
    cnpj                VARCHAR(18)   NULL,
    cpf                 VARCHAR(14)   NULL,
    inscricao_estadual  VARCHAR(30)   NULL,
    nome_contato        VARCHAR(100)  NULL,
    cargo          VARCHAR(80)   NULL,
    endereco       VARCHAR(200)  NULL,
    bairro         VARCHAR(100)  NULL,
    cep            VARCHAR(9)    NULL,
    cidade         VARCHAR(80)   NULL,
    estado         VARCHAR(2)    NULL,
    email          VARCHAR(150)  NULL,
    telefone       VARCHAR(20)   NULL,
    telefone2      VARCHAR(20)   NULL,
    consultor      VARCHAR(100)  NULL,
    valor          DECIMAL(15,2) NULL,
    status         VARCHAR(50)   NULL,
    data_entrada   DATE          NULL,
    prorrogacao    DATE          NULL,
    responsavel    VARCHAR(100)  NULL,
    criado_por     BIGINT        NULL,
    observacoes    TEXT          NULL,
    criado_em      DATETIME(6)   NOT NULL,
    atualizado_em  DATETIME(6)   NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_clientes_status     (status),
    INDEX idx_clientes_consultor  (consultor),
    INDEX idx_clientes_criado_por (criado_por),
    INDEX idx_clientes_empresa    (empresa),
    CONSTRAINT fk_clientes_criado_por FOREIGN KEY (criado_por)
        REFERENCES usuarios (id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------------
-- OCORRÊNCIAS — histórico de atendimento por cliente
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ocorrencias (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    cliente_id     BIGINT       NOT NULL,
    usuario        VARCHAR(100) NOT NULL COMMENT 'Nome do usuário responsável',
    status         VARCHAR(80)  NOT NULL DEFAULT 'Aberto',
    estagio        VARCHAR(50)  NULL     COMMENT 'Quente | Morno | Frio',
    agendamento    DATETIME     NULL     COMMENT 'Data/hora do próximo contato agendado',
    informacoes    TEXT         NOT NULL,
    criado_em      DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_ocorrencias_cliente_id (cliente_id),
    INDEX idx_ocorrencias_agendamento (agendamento),
    CONSTRAINT fk_ocorrencias_cliente FOREIGN KEY (cliente_id)
        REFERENCES clientes (id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------------
-- AGENDA — tarefas do dashboard por usuário
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS agenda (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    usuario_id     BIGINT       NOT NULL,
    cliente_nome   VARCHAR(150) NOT NULL,
    hora           TIME         NOT NULL,
    tipo           VARCHAR(100) NOT NULL COMMENT 'Ex: Ligação, Proposta, Reunião',
    data           DATE         NOT NULL COMMENT 'Data da tarefa',
    concluida      TINYINT(1)   NOT NULL DEFAULT 0,
    criado_em      DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_agenda_usuario_id (usuario_id),
    INDEX idx_agenda_data       (data),
    CONSTRAINT fk_agenda_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
