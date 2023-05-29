/* NOTES
- BY DEFAULT AS IDENTIFY - https://www.postgresqltutorial.com/postgresql-tutorial/postgresql-identity-column/#:~:text=issue%20an%20error.-,The%20GENERATED%20BY%20DEFAULT,-also%20instructs%20PostgreSQL
- Reserved keywords - https://www.postgresql.org/docs/current/sql-keywords-appendix.html
- TIMESTAMP - https://www.postgresql.org/docs/current/datatype-datetime.html
*/
CREATE TYPE regiao_enum AS ENUM ('EU', 'NA', 'ASIA');
CREATE TYPE estado_enum AS ENUM ('Ativo', 'Inativo', 'Banido');

CREATE TABLE REGIAO(
    id regiao_enum PRIMARY KEY
);

CREATE TABLE JOGADOR(
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    estado estado_enum NOT NULL,
    regiao regiao_enum NOT NULL,
    CONSTRAINT fk_regiao FOREIGN KEY (regiao) REFERENCES REGIAO(id)
);

CREATE TABLE AMIGOS(
    id_jogador_pedinte INT NOT NULL,
    id_jogador_destino INT NOT NULL,
    aceite BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_id_jogador_pedinte FOREIGN KEY (id_jogador_pedinte) REFERENCES JOGADOR(id),
    CONSTRAINT fk_id_jogador_destino FOREIGN KEY (id_jogador_destino) REFERENCES JOGADOR(id),
    PRIMARY KEY (id_jogador_pedinte, id_jogador_destino)
);

CREATE TABLE CHAT_GROUP(
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    nome TEXT NOT NULL,
    id_criador INT NOT NULL,
    CONSTRAINT fk_id_criador FOREIGN KEY (id_criador) REFERENCES JOGADOR(id)
);

CREATE TABLE CHAT_GROUP_PARTICIPANT(
    id_chat_group INT NOT NULL,
    id_jogador INT NOT NULL,
    CONSTRAINT fk_id_chat_group FOREIGN KEY (id_chat_group) REFERENCES CHAT_GROUP(id),
    CONSTRAINT fk_id_jogador FOREIGN KEY (id_jogador) REFERENCES JOGADOR(id),
    PRIMARY KEY (id_chat_group, id_jogador)
);

CREATE TABLE CHATS(
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    id_group INT NOT NULL,
    dateAndTime TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP(4) NOT NULL, --4 is the precision value of the milli seconds
    mensagem TEXT NOT NULL,
    id_jogador INT NOT NULL,
    CONSTRAINT fk_id FOREIGN KEY (id_group) REFERENCES CHAT_GROUP(id),
    CONSTRAINT fk_id_jogador FOREIGN KEY (id_jogador) REFERENCES JOGADOR(id)
);

CREATE TABLE JOGO(
    id CHAR(10) PRIMARY KEY CHECK(LENGTH(id)=10), --"Os jogos têm como identificador uma referência alfanumérica de dimensão 10"
    url TEXT NOT NULL UNIQUE,
    nome TEXT NOT NULL UNIQUE
);

CREATE TABLE COMPRA(
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    dataa TIMESTAMP NOT NULL, --"dataa" because 'data' is a reserved word
    preco DECIMAL(6, 2) NOT NULL, -- Up to 9999.00
    id_jogador INT NOT NULL,
    id_jogo CHAR(10) NOT NULL,
    CONSTRAINT fk_id_jogador FOREIGN KEY (id_jogador) REFERENCES JOGADOR(id),
    CONSTRAINT fk_id_jogo FOREIGN KEY (id_jogo) REFERENCES JOGO(id)
);

CREATE TABLE CRACHA(
    id_jogo CHAR(10) NOT NULL,
    nome TEXT NOT NULL,
    pontosAssociados INT NOT NULL,
    url TEXT NOT NULL,
    CONSTRAINT fk_id_jogo FOREIGN KEY (id_jogo) REFERENCES JOGO(id),
    PRIMARY KEY(id_jogo, nome) --"interessa registar o nome do crachá que é único para cada jogo"
);

CREATE TABLE CRACHAS_ATRIBUIDOS(
    id_jogador INT NOT NULL,
    id_jogo CHAR(10) NOT NULL,
    nome TEXT NOT NULL,
    CONSTRAINT fk_id_jogo FOREIGN KEY (id_jogador) REFERENCES JOGADOR(id),
    CONSTRAINT fk_keys_crachas FOREIGN KEY (id_jogo, nome) REFERENCES CRACHA(id_jogo, nome),
    PRIMARY KEY (id_jogador, id_jogo, nome)
);

CREATE TABLE PARTIDA(
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    id_jogo CHAR(10) NOT NULL,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP, --it can be nullable so we can know if the partida finished
    regiao regiao_enum NOT NULL,
    CONSTRAINT  fk_id_jogo FOREIGN KEY (id_jogo) REFERENCES JOGO(id),
    CONSTRAINT fk_regiao FOREIGN KEY (regiao) REFERENCES REGIAO(id)
);

CREATE TABLE PARTIDA_NORMAL(
    id_partida INT PRIMARY KEY NOT NULL,
    dificuldade INT NOT NULL CHECK(dificuldade IN (1, 2, 3, 4, 5)), --"As partidas normais devem ter informação sobre o grau de dificuldade (valor de 1 a 5)"
    CONSTRAINT fk_id_partida FOREIGN KEY (id_partida) REFERENCES PARTIDA(id)
);

CREATE TABLE PARTIDA_MULTIJOGADOR(
    id_partida INT PRIMARY KEY NOT NULL,
    estado TEXT NOT NULL CHECK(estado IN ('Por iniciar', 'A aguardar jogadores', 'Em curso', 'Terminada')), --"[As partidas multijogador, ...] Devem ainda conter informação sobre o estado em que se encontram"
    CONSTRAINT fk_id_partida FOREIGN KEY (id_partida) REFERENCES PARTIDA(id)
);

--"As partidas normais devem estar (...) estar associadas ao jogador que as joga e à pontuação por ele obtida" sendo necessário guardar as pontuações obtidas por cada jogador em cada partida.
--As partidas multi-jogador devem estar associadas aos jogadores que as jogam sendo necessário guardar as pontuações obtidas por cada jogador em cada partida
CREATE TABLE PONTUACAO_JOGADOR(
    id_partida INT NOT NULL,
    id_jogador INT NOT NULL,
    pontos INT NOT NULL,
    CONSTRAINT fk_id_jogador FOREIGN KEY (id_jogador) REFERENCES JOGADOR(id),
    CONSTRAINT fk_id_partida FOREIGN KEY (id_partida) REFERENCES PARTIDA(id),
    PRIMARY KEY(id_partida, id_jogador)
);

CREATE TABLE ESTATISTICA_JOGADOR( -- Interessa registar para cada jogador, o número de partidas que efetuou, o número de jogos diferentes que jogou e o total de pontos de todos os jogos e partidas efetuadas
    id_jogador INT NOT NULL,
    numJogosQueComprou INT NOT NULL,
    numPartidas INT NOT NULL,
    totalPontos INT NOT NULL,
    CONSTRAINT fk_id_jogador FOREIGN KEY (id_jogador) REFERENCES JOGADOR(id),
    PRIMARY KEY(id_jogador)
);

CREATE TABLE ESTATISTICA_JOGO(--Para cada jogo interessa registar o número de partidas, o número de jogadores e o total de pontos
    id_jogo CHAR(10),
    totalPartidas INT NOT NULL,
    numJogadoresCompraram INT NOT NULL,
    totalPontos INT NOT NULL,
    PRIMARY KEY(id_jogo)
);