CREATE DATABASE IF NOT EXISTS crm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE crm;

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
    cnpj           VARCHAR(18)   NULL,
    cpf            VARCHAR(14)   NULL,
    nome_contato   VARCHAR(100)  NULL,
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
    INDEX idx_clientes_empresa    (empresa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Usuário admin inicial — senha: admin123  (TROQUE no primeiro login!)
INSERT IGNORE INTO usuarios (login, nome, senha, cargo, ativo, criado_em, atualizado_em)
VALUES ('admin','Administrador','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','admin',1,NOW(),NOW());
