/* NOTES
- BY DEFAULT AS IDENTIFY - https://www.postgresqltutorial.com/postgresql-tutorial/postgresql-identity-column/#:~:text=issue%20an%20error.-,The%20GENERATED%20BY%20DEFAULT,-also%20instructs%20PostgreSQL
- Reserved keywords - https://www.postgresql.org/docs/current/sql-keywords-appendix.html
- TIMESTAMP - https://www.postgresql.org/docs/current/datatype-datetime.html
*/
CREATE TYPE regiao_enum AS ENUM ('EU', 'NA', 'ASIA');

CREATE TABLE REGIAO(
    id regiao_enum PRIMARY KEY
);

CREATE TABLE JOGADOR(
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    estado TEXT NOT NULL CHECK(estado IN ('Ativo', 'Inativo', 'Banido')),
    regiao regiao_enum NOT NULL,
    CONSTRAINT fk_regiao FOREIGN KEY regiao REFERENCES REGIAO(id)
);

CREATE TABLE JOGO(
    id CHAR(10) PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY, --AKA referencia
    url TEXT NOT NULL UNIQUE,
    nome TEXT NOT NULL UNIQUE
);

CREATE TABLE COMPRA(
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    dataa TIMESTAMP NOT NULL,
    preco DECIMAL(6, 2) -- Up to 1000.00
    id_jogador INT NOT NULL,
    id_jogo CHAR(10) NOT NULL,
    CONSTRAINT fk_id_jogador FOREIGN KEY id_jogador REFERENCES JOGADOR(id),
    CONSTRAINT fk_id_jogo FOREIGN KEY id_jogo REFERENCES JOGO(id)
);

CREATE TABLE CRACHA(
    id_jogo CHAR(10) NOT NULL
    nome TEXT NOT NULL,
    pontos INT NOT NULL,
    url TEXT NOT NULL,
    CONSTRAINT fk_id_jogador FOREIGN KEY id_jogador REFERENCES JOGADOR(id),
    CONSTRAINT fk_id_jogo FOREIGN KEY id_jogo REFERENCES JOGO(id),
    PRIMARY KEY(id_jogo, nome)
);

CREATE TABLE CRACHAS_ATRIBUIDOS(
    id_jogador INT NOT NULL,
    id_cracha INT NOT NULL UNIQUE,
    CONSTRAINT fk_id_jogador FOREIGN KEY id_jogador REFERENCES JOGADOR(id),
    PRIMARY KEY (id_jogador, id_cracha)
);

CREATE TABLE PARTIDA(
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    id_jogo INT NOT NULL,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP NOT NULL,
    regiao regiao_enum NOT NULL,
    CONSTRAINT fk_regiao FOREIGN KEY regiao REFERENCES REGIAO(id),
    CONSTRAINT  fk_id_jogo FOREIGN KEY id_jogo REFERENCES JOGO(id)
);

CREATE TABLE PONTUACAO_JOGADOR(
    id_partida INT NOT NULL,
    id_jogador INT NOT NULL,
    pontos INT NOT NULL,
    CONSTRAINT fk_id_jogador FOREIGN KEY id_jogador REFERENCES JOGADOR(id),
    CONSTRAINT fk_id_partida FOREIGN KEY id_partida REFERENCES PARTIDA(id),
    PRIMARY KEY(id_partida, id_jogador)
);

CREATE TABLE PARTIDA_NORMAL(
    id_partida INT NOT NULL,
    dificuldade TEXT NOT NULL CHECK(dificuldade IN (1, 2, 3, 4, 5)),
    CONSTRAINT fk_id_partida FOREIGN KEY id_partida REFERENCES PARTIDA(id)
);

CREATE TABLE PARTIDA_MULTIJOGADOR(
    id_partida INT NOT NULL,
    estado TEXT NOT NULL CHECK(estado IN ('Por iniciar', 'A aguardar jogadores', 'Em curso', 'Terminada')),
    CONSTRAINT fk_id_partida FOREIGN KEY id_partida REFERENCES PARTIDA(id)
);