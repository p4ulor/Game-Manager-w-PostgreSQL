DO $$ BEGIN END; $$ LANGUAGE PLPGSQL; --for github indexing
--Deletes tables (and removes data)

DROP PROCEDURE IF EXISTS criarJogador; --d)
DROP PROCEDURE IF EXISTS mudarEstadoJogador; --d)
DROP FUNCTION IF EXISTS getTotalPointsOfPlayer; --e)
DROP FUNCTION IF EXISTS getNumGames_aPlayerPlayed; --f)
DROP FUNCTION IF EXISTS gamePointsPerPlayer; --g)

DROP PROCEDURE IF EXISTS associarCrachaTansaction; --h)
DROP PROCEDURE IF EXISTS associarCrachaTansacao; --h)
DROP PROCEDURE IF EXISTS associarCrachaLogica; --h)

DROP PROCEDURE IF EXISTS iniciarConversa; --i)
DROP PROCEDURE IF EXISTS iniciarConversaTransacao; --i)
DROP PROCEDURE IF EXISTS iniciarConversaLogica; --i)

DROP PROCEDURE IF EXISTS joinToGroupChat;--j)

DROP PROCEDURE IF EXISTS enviarMensagem; --k)
DROP PROCEDURE IF EXISTS enviarMensagemTransacao; --k)
DROP PROCEDURE IF EXISTS enviarMensagemLogica; --k)

DROP VIEW IF EXISTS allInfoPlayer; --l

DROP TRIGGER IF EXISTS autoGiveBadges ON PARTIDA; --m, must be done first because this trigger uses giveBadge
DROP FUNCTION IF EXISTS giveBadge; --m

DROP FUNCTION IF EXISTS jogadorTotalInfoDeleteFunction; --n

DROP TABLE IF EXISTS ESTATISTICA_JOGO;
DROP TABLE IF EXISTS ESTATISTICA_JOGADOR;
DROP TABLE IF EXISTS PONTUACAO_JOGADOR;
DROP TABLE IF EXISTS PARTIDA_MULTIJOGADOR;
DROP TABLE IF EXISTS PARTIDA_NORMAL;
DROP TABLE IF EXISTS PARTIDA;
DROP TABLE IF EXISTS CRACHAS_ATRIBUIDOS;
DROP TABLE IF EXISTS CRACHA;
DROP TABLE IF EXISTS COMPRA;
DROP TABLE IF EXISTS JOGO;
DROP TABLE IF EXISTS CHATS;
DROP TABLE IF EXISTS CHAT_GROUP_PARTICIPANT;
DROP TABLE IF EXISTS CHAT_GROUP;
DROP TABLE IF EXISTS AMIGOS;
DROP TABLE IF EXISTS JOGADOR;
DROP TABLE IF EXISTS REGIAO;

DROP TYPE IF EXISTS regiao_enum;
DROP TYPE IF EXISTS estado_enum;

---------------------------------------------

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

----------------------------

INSERT INTO REGIAO VALUES ('EU');
INSERT INTO REGIAO VALUES ('NA');
INSERT INTO REGIAO VALUES ('ASIA');

INSERT INTO JOGADOR VALUES(0, 'paulo', 'paulo@mail.com', 'Ativo', 'EU');
INSERT INTO JOGADOR VALUES(1, 'miguel', 'miguel@mail.com', 'Ativo', 'EU');
INSERT INTO JOGADOR VALUES(2, 'tyler1', 'tyler1@mail.com', 'Banido', 'NA');
INSERT INTO JOGADOR VALUES(3, 'noobmaster59', 'noobmaster59@mail.com', 'Inativo', 'ASIA');

ALTER SEQUENCE JOGADOR_id_seq RESTART WITH 4;

INSERT INTO AMIGOS VALUES(0, 1, TRUE);
INSERT INTO AMIGOS VALUES(3, 2, FALSE);

--id, nome, id_criador, participantes
INSERT INTO CHAT_GROUP VALUES(0, 'squad', 0);
ALTER SEQUENCE chat_group_id_seq RESTART WITH 1;
INSERT INTO CHAT_GROUP_PARTICIPANT VALUES (0, 0);

--id, id_group, dateAndTime, mensagem
INSERT INTO CHATS VALUES(DEFAULT, 0, '2023-05-26 19:10:25-07', 'ay', 0);
INSERT INTO CHATS VALUES(DEFAULT, 0, '2023-05-26 19:10:35-07', 'boas', 0);

--id, nome
INSERT INTO JOGO VALUES('abcefghij0', 'skribll', 'https://skribbl.io/'); --comprado por paulo
INSERT INTO JOGO VALUES('abcefghij1', 'Age Of War', 'https://www.crazygames.com/game/age-of-war'); --comprado por paulo e miguel
INSERT INTO JOGO VALUES('abcefghij2', 'Minecraft', 'https://www.minecraft.net/pt-pt'); --comprado pelo miguel
INSERT INTO JOGO VALUES('abcefghij3', 'Mortal Shell', 'https://store.steampowered.com/app/1110910/Mortal_Shell/'); --nao comprado por nimguem
INSERT INTO JOGO VALUES('abcefghij4', 'Prototype', 'https://store.steampowered.com/app/10150/Prototype/'); --nao comprado por nimguem

--id, dataa, preco, id_jogador, id_jogo
INSERT INTO COMPRA VALUES(DEFAULT, '2023-05-26 00:00:00-00', 1.00, 0, 'abcefghij0'); --paulo compra skribbl
INSERT INTO COMPRA VALUES(DEFAULT, '2023-05-26 00:00:00-00', 9999.00, 0, 'abcefghij1'); --paulo compra Age Of War
INSERT INTO COMPRA VALUES(DEFAULT, '2023-05-26 00:00:00-00', 9999.00, 1, 'abcefghij1'); --miguel compra Age Of War
INSERT INTO COMPRA VALUES(DEFAULT, '2023-05-26 00:00:00-00', 9.00, 1, 'abcefghij2'); --miguel compra Minecraft

--id_jogo, nome, pontosAssociados
INSERT INTO CRACHA VALUES('abcefghij0', 'GOAT', 10000, 'https://knowyourmeme.com/memes/thats-why-hes-the-goat'); --achivement for Minecraft
INSERT INTO CRACHA VALUES ('abcefghij1', 'Sensational', 5000, 'https://youtu.be/3JiUJm3sAv8'); --achivement for Age Of War
INSERT INTO CRACHA VALUES('abcefghij3', 'Mortal Shell achievement', 2000, 'https://knowyourmeme.com/memes/what-was-he-cooking'); --achivement for Mortal Shell

--id_jogador, id_jogo, nome
INSERT INTO CRACHAS_ATRIBUIDOS VALUES(0, 'abcefghij0', 'GOAT');
INSERT INTO CRACHAS_ATRIBUIDOS VALUES(1, 'abcefghij1', 'Sensational');

--id, id_jogo, data_inicio, data_fim, regiao
INSERT INTO PARTIDA VALUES(0, 'abcefghij1', '2023-05-26 10:00:00-00', '2023-05-26 12:00:00-00', 'EU'); --Age Of War, normal, paulo
INSERT INTO PARTIDA VALUES(1, 'abcefghij1', '2023-05-26 13:00:00-00', '2023-05-26 14:00:00-00', 'EU'); --Age Of War, multi, paulo
INSERT INTO PARTIDA VALUES(2, 'abcefghij1', '2023-05-26 15:00:00-00', '2023-05-26 16:00:00-00', 'EU'); --Age Of War, multi, miguel
INSERT INTO PARTIDA VALUES(3, 'abcefghij2', '2023-05-27 15:00:00-00', '2023-05-27 18:00:00-00', 'EU'); --Minecraft, normal, paulo

ALTER SEQUENCE PARTIDA_id_seq RESTART WITH 4;

--id_partida, dificuldade
INSERT INTO PARTIDA_NORMAL VALUES(0, 5); --Age Of War, normal, paulo
INSERT INTO PARTIDA_NORMAL VALUES(3, 5); --Minecraft, normal, paulo

--id_partida, estado
INSERT INTO PARTIDA_MULTIJOGADOR VALUES(1, 'Terminada'); --Age Of War, multi, paulo
INSERT INTO PARTIDA_MULTIJOGADOR VALUES(2, 'Terminada'); --Age Of War, multi, miguel

--id_partida, id_jogador, pontos
INSERT INTO PONTUACAO_JOGADOR VALUES(0, 0, 10000);  --Age Of War, normal, paulo
INSERT INTO PONTUACAO_JOGADOR VALUES(1, 0, 9000); --Age Of War, multi, paulo
INSERT INTO PONTUACAO_JOGADOR VALUES(2, 1, 5000); --Age Of War, multi, miguel
INSERT INTO PONTUACAO_JOGADOR VALUES(3, 0, 1000); --Minecraft, normal, paulo

--id_jogador, numJogosQueComprou, numPartidas, totalPontos
INSERT INTO ESTATISTICA_JOGADOR VALUES(0, 1, 2, 20000); -- estatisticas paulo
INSERT INTO ESTATISTICA_JOGADOR VALUES(1, 1, 1, 5000); -- estatisticas miguel

--id_jogo, totalPartidas, numJogadoresCompraram, totalPontos,
INSERT INTO ESTATISTICA_JOGO VALUES('abcefghij1', 3, 2, 24000); -- estatisticas Age of War
INSERT INTO ESTATISTICA_JOGO VALUES('abcefghij2', 1, 1, 1000); -- estatisticas minecraft

--------------------- d)

CREATE OR REPLACE PROCEDURE criarJogador(username TEXT, email TEXT, regiao regiao_enum) language plpgsql as $$
    declare
        next_id INT;
        attempts INT := 0;
        max_attempts INT := 10;
    begin
        LOOP
            attempts := attempts + 1;
            IF attempts > max_attempts THEN
                RAISE EXCEPTION 'Could not insert new jogador after % attempts', max_attempts;
            END IF;
            
            SELECT NEXTVAL('jogador_id_seq') INTO next_id;
            RAISE NOTICE 'next_id (%)', next_id;
            
            begin
                INSERT INTO JOGADOR VALUES(next_id, username, email, 'Ativo', regiao);
                EXCEPTION --must be inside begin-end
                WHEN unique_violation THEN
                    begin
                        RAISE NOTICE 'Duplicate (%)', next_id;
                        next_id := NULL;
                    end;
            end;
            
            EXIT WHEN next_id IS NOT NULL;
        END LOOP;
    end;
$$;

--desativar ou banir o jogador, dados os seus email, região e username:
CREATE OR REPLACE PROCEDURE mudarEstadoJogador(usernamee TEXT, novoEstado estado_enum) language plpgsql as $$
    begin
        UPDATE JOGADOR
        SET estado = novoEstado
        WHERE username = usernamee;
    end;
$$;

--------------------- e)

CREATE OR REPLACE FUNCTION getTotalPointsOfPlayer(id INT) RETURNS INT language plpgsql as $$
    declare
	total INT := 0;
	begin
        SELECT COALESCE(SUM(pontos), 0) --coalesce https://www.postgresqltutorial.com/postgresql-aggregate-functions/postgresql-sum-function/
		FROM PONTUACAO_JOGADOR 
		WHERE id_jogador = id INTO total;
		
		RETURN total;
    end;
$$;

------------------------------- f)

CREATE OR REPLACE FUNCTION getNumGames_aPlayerPlayed(idJogador INT) RETURNS INT language plpgsql as $$
	declare
    count_id_jogo integer;
	begin
		SELECT COUNT(DISTINCT PARTIDA.id_jogo)
		INTO count_id_jogo
		FROM PARTIDA
		INNER JOIN PONTUACAO_JOGADOR ON PARTIDA.id = PONTUACAO_JOGADOR.id_partida AND PONTUACAO_JOGADOR.id_jogador = idJogador;
		
		RETURN count_id_jogo;
	end;
$$;

------------------------------------- g)

CREATE OR REPLACE FUNCTION gamePointsPerPlayer(idJogo char(10))
    RETURNS TABLE (idJogador INT, totalPontos INT)
    LANGUAGE PLPGSQL
    AS
    $$
    BEGIN
        RETURN QUERY
            SELECT id_jogador, SUM(pontos)::INT -- SUM returns BIGINT but function's second parameter signature is INT so a cast to INT is used
            FROM PONTUACAO_JOGADOR INNER JOIN PARTIDA ON PONTUACAO_JOGADOR.id_partida = PARTIDA.id
            WHERE id_jogo = idJogo
            GROUP BY id_jogador;
    END;
    $$;
	
------------------------------------------ h)

 CREATE OR REPLACE PROCEDURE associarCrachaTansaction(idJogador JOGADOR.id%TYPE, idJogo JOGO.id%TYPE,
                                                     nomeCracha CRACHA.nome%TYPE)
    LANGUAGE PLPGSQL
    AS
    $$
    BEGIN
        COMMIT;
        SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
        CALL associarCrachaTansacao(idJogador, idJogo, nomeCracha);
    END;
    $$;

CREATE OR REPLACE PROCEDURE associarCrachaTansacao(idJogador JOGADOR.id%TYPE, idJogo JOGO.id%TYPE,
                                                     nomeCracha CRACHA.nome%TYPE)
    LANGUAGE PLPGSQL
    AS
    $$
    DECLARE
        code char(5) DEFAULT '00000';
        msg  text;
    BEGIN
        CALL associarCrachaLogica(idJogador, idJogo, nomeCracha);
        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Ocorreu algum erro';
                GET STACKED DIAGNOSTICS
                    code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
                RAISE NOTICE 'code=%, msg=%',code,msg;
                ROLLBACK;

    END;
    $$;

CREATE OR REPLACE PROCEDURE associarCrachaLogica(idJogador JOGADOR.id%TYPE, idJogo JOGO.id%TYPE, nomeCracha CRACHA.nome%TYPE)
    LANGUAGE PLPGSQL
    AS
    $$
    DECLARE points PONTUACAO_JOGADOR.pontos%TYPE;
    DECLARE badge_points CRACHA.pontosAssociados%TYPE;
    DECLARE test_var boolean;
    BEGIN
        SELECT pontosassociados INTO badge_points FROM CRACHA WHERE id_jogo = idJogo AND nomeCracha = nome;
        RAISE NOTICE 'badge points: %', badge_points;
        IF badge_points IS NULL then
            RAISE NOTICE 'Did not insert as there are no badges with the chosen characteristics.';
            RETURN; -- maybe exception here
        END IF;

        SELECT totalPontos INTO points FROM PontosJogoPorJogador(idJogo) WHERE PontosJogoPorJogador.idjogador = associarCrachaLogica.idJogador;
        RAISE NOTICE 'points: %', points;
        IF points IS NULL then
            RAISE NOTICE 'Did not insert as there are no points in the chosen game for the chosen player.';
            RETURN; -- maybe exception here
        END IF;


        IF points >= badge_points THEN
            SELECT EXISTS(SELECT * FROM CRACHAS_ATRIBUIDOS WHERE id_jogador = idJogador AND id_jogo = idJogo and nome = nomeCracha) INTO test_var;
            IF NOT test_var then
                RAISE NOTICE 'Associating cracha.';
                INSERT INTO CRACHAS_ATRIBUIDOS VALUES (idJogador, idJogo, nomeCracha);
            ELSE
                RAISE NOTICE 'Cracha already associated.';
            END IF;
        END IF;
    END; -- TODO: For some reason this line is givin error in INTELLIJ linting, but it runs well
    $$;
	

------------------------------------------------- i)

 CREATE OR REPLACE PROCEDURE iniciarConversa(idJogador JOGADOR.id%TYPE, nomeConversa CHAT_GROUP.nome%TYPE)
    LANGUAGE PLPGSQL
    AS
    $$
    BEGIN
        COMMIT;
        SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
        CALL iniciarConversaTransacao(idJogador, nomeConversa);
    END;
    $$;


CREATE OR REPLACE PROCEDURE iniciarConversaTransacao(idJogador JOGADOR.id%TYPE, nomeConversa CHAT_GROUP.nome%TYPE)
    LANGUAGE PLPGSQL
    AS
    $$
    DECLARE
        code char(5) DEFAULT '00000';
        msg  text;
    BEGIN
        CALL iniciarConversaLogica(idJogador, nomeConversa);
        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Ocorreu algum erro';
                GET STACKED DIAGNOSTICS
                    code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
                RAISE NOTICE 'code=%, msg=%',code,msg;
                ROLLBACK;

    END;
    $$;

CREATE OR REPLACE PROCEDURE iniciarConversaLogica(idJogador JOGADOR.id%TYPE, nomeConversa CHAT_GROUP.nome%TYPE)
    LANGUAGE PLPGSQL
    AS
    $$
    BEGIN
        IF NOT EXISTS (SELECT * FROM jogador where jogador.id = idJogador) then
            RAISE EXCEPTION 'Player does not exist %', idJogador;
        END IF;

        INSERT INTO chat_group VALUES (DEFAULT, nomeConversa, idJogador);
    END;
    $$;

CREATE OR REPLACE FUNCTION chatGroupCreatorIsParticipant() RETURNS TRIGGER
    LANGUAGE PLPGSQL
    AS
    $$
    BEGIN
        IF tg_op <> 'INSERT' THEN
            RAISE EXCEPTION 'Invalid Trigger';
        END IF;

        INSERT INTO CHAT_GROUP_PARTICIPANT values (new.id, new.id_criador);
        RETURN NULL;
    END;
    $$;

CREATE OR REPLACE TRIGGER chatGroupCreatorIsParticipantTrigger
    AFTER INSERT ON CHAT_GROUP
    FOR EACH ROW
    EXECUTE FUNCTION chatGroupCreatorIsParticipant();

---------------------------------------- j)

CREATE OR REPLACE PROCEDURE joinToGroupChat(idJogador JOGADOR.id%TYPE, id_group CHAT_GROUP.id%TYPE) LANGUAGE plpgsql AS $$
    BEGIN
        IF NOT EXISTS (SELECT id FROM jogador where jogador.id = idJogador) THEN
            RAISE EXCEPTION 'Player does not exist %', idJogador;
        END IF;

        IF NOT EXISTS (SELECT id FROM CHAT_GROUP where CHAT_GROUP.id = id_group) THEN
            RAISE EXCEPTION 'Chat group does not exist %', id_group;
        END IF;

        INSERT INTO CHAT_GROUP_PARTICIPANT VALUES(id_group, idJogador);
    END;
$$;

----------------------------------------- k)

  CREATE OR REPLACE PROCEDURE enviarMensagem(idJogador JOGADOR.id%TYPE, idConversa CHAT_GROUP.id%TYPE, mensagem CHATS.mensagem%TYPE)
    LANGUAGE PLPGSQL
    AS
    $$
    BEGIN
        COMMIT;
        SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
        CALL enviarMensagemTransacao(idJogador, idConversa, mensagem);
    END;
    $$;

  CREATE OR REPLACE PROCEDURE enviarMensagemTransacao(idJogador JOGADOR.id%TYPE, idConversa CHAT_GROUP.id%TYPE, mensagem CHATS.mensagem%TYPE)
    LANGUAGE PLPGSQL
    AS
    $$
    DECLARE
        code char(5) DEFAULT '00000';
        msg  text;
    BEGIN
        CALL enviarMensagemLogica(idJogador, idConversa, mensagem);
        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Ocorreu algum erro';
                GET STACKED DIAGNOSTICS
                    code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
                RAISE NOTICE 'code=%, msg=%',code,msg;
                ROLLBACK;
    END;
    $$;

  CREATE OR REPLACE PROCEDURE enviarMensagemLogica(idJogador JOGADOR.id%TYPE, idConversa CHAT_GROUP.id%TYPE, mensagem CHATS.mensagem%TYPE)
    LANGUAGE PLPGSQL
    AS
    $$
    BEGIN
        IF NOT EXISTS (SELECT * FROM jogador where jogador.id = idJogador) then
            RAISE EXCEPTION 'Player does not exist %', idJogador;
        END IF;

        IF NOT EXISTS (SELECT * FROM CHAT_GROUP where CHAT_GROUP.id = idConversa) then
            RAISE EXCEPTION 'Chat group does not exist %', idConversa;
        END IF;

        IF NOT EXISTS (SELECT * FROM CHAT_GROUP_PARTICIPANT where CHAT_GROUP_PARTICIPANT.id_chat_group = idConversa AND CHAT_GROUP_PARTICIPANT.id_jogador = idJogador) then
            RAISE EXCEPTION 'Player % is not participant of chosen chat group %', idJogador, idConversa;
        END IF;

        INSERT INTO CHATS VALUES (DEFAULT, enviarMensagemLogica.idConversa, DEFAULT, enviarMensagemLogica.mensagem, enviarMensagemLogica.idJogador);

    END;
    $$;

---------------------------------- L)

CREATE OR REPLACE VIEW allInfoPlayer AS 
    SELECT id, estado, email, username, numGamesPlayed, totalPoints, numtotalpartidas
    FROM JOGADOR
    
    FULL OUTER JOIN(
        SELECT id_jogador, COALESCE(SUM(pontos), 0) AS totalPoints
        FROM PONTUACAO_JOGADOR
		GROUP BY id_jogador
    ) AS POINTS ON POINTS.id_jogador = JOGADOR.id

    FULL OUTER JOIN(
        SELECT PONTUACAO_JOGADOR.id_jogador AS id_jogador, COUNT(DISTINCT PARTIDA.id_jogo) AS numGamesPlayed, COUNT(PARTIDA.id_jogo) AS numtotalpartidas
		FROM PARTIDA
		INNER JOIN PONTUACAO_JOGADOR ON PARTIDA.id = PONTUACAO_JOGADOR.id_partida
		GROUP BY id_jogador
    ) AS gamesPlayed ON gamesPlayed.id_jogador = JOGADOR.id
	
	WHERE estado !='Banido';

----------------------------------- M)

CREATE OR REPLACE FUNCTION giveBadge() RETURNS TRIGGER LANGUAGE PLPGSQL AS $$ --must return trigger
    DECLARE
        jogadoresEpontos PONTUACAO_JOGADOR;
        nomeCracha TEXT := '';
    BEGIN

        RAISE NOTICE 'TRIGGERED giveBadge';
        
        FOR jogadoresEpontos IN( --built-in/implicit cursor https://dba.stackexchange.com/a/35722
            SELECT *
            FROM PONTUACAO_JOGADOR 
            WHERE id_partida = NEW.id
        )

        LOOP

            --https://www.postgresqltutorial.com/postgresql-window-function/postgresql-first_value-function/
            SELECT val FROM (
                SELECT FIRST_VALUE(nome) OVER (ORDER BY CRACHA.pontosAssociados ASC) AS val
                FROM CRACHA
                WHERE CRACHA.pontosAssociados >= jogadoresEpontos.pontos
            ) AS subquery
            LIMIT 1
            INTO nomeCracha;
			
			BEGIN
            RAISE NOTICE 'Will insert (%), (%), (%) in CRACHAS_ATRIBUIDOS', jogadoresEpontos.id_jogador, NEW.id_jogo, nomeCracha;
            INSERT INTO CRACHAS_ATRIBUIDOS VALUES(jogadoresEpontos.id_jogador, NEW.id_jogo, nomeCracha);
            EXCEPTION --must be inside begin-end
            WHEN unique_violation THEN
                BEGIN
                    RAISE NOTICE 'CRACHAS (%) already given to (%) for game (%)', jogadoresEpontos.id_jogador, NEW.id_jogo, nomeCracha;
                END;
			END;
			
        END LOOP;
		RETURN NULL; --https://www.postgresql.org/docs/current/plpgsql-trigger.html#:~:text=A%20trigger%20function%20must%20return%20either%20NULL%20or%20a%20record/row%20value%20having%20exactly%20the%20structure%20of%20the%20table%20the%20trigger%20was%20fired%20for.
    END;
$$;

CREATE OR REPLACE TRIGGER autoGiveBadges AFTER UPDATE ON PARTIDA
    FOR EACH ROW
    WHEN(
        DATE(NEW.data_fim) = CURRENT_DATE -- CURRENT_DATE is reserved word
        OR DATE(NEW.data_fim) = CURRENT_DATE - INTEGER '1' --this is because in the case the game starts in a day and finishes in the next
    )
    
    -- Arguments of the procedure of an execute must be strings aparently https://www.postgresql.org/docs/current/sql-createtrigger.html#:~:text=The%20arguments%20are%20literal%20string%20constants
    --https://stackoverflow.com/a/49120692
    -- CAST (PARTIDA.id AS TEXT), CAST (PARTIDA.id_jogo AS TEXT) doesnt work
    EXECUTE FUNCTION giveBadge() --must be a function, it wasn't working if it was procedure
; 

----------------------------------------------------------- N)

CREATE OR REPLACE FUNCTION jogadorTotalInfoDeleteFunction() RETURNS TRIGGER
    LANGUAGE PLPGSQL
    AS
    $$
    BEGIN
        IF tg_op <> 'DELETE' THEN
            RAISE EXCEPTION 'Invalid Trigger';
        END IF;

    UPDATE JOGADOR SET estado = 'Banido' WHERE JOGADOR.id = old.id;
    RETURN NULL;
    END;
    $$;

CREATE OR REPLACE TRIGGER jogadorTotalInfoDeleteTrigger
    INSTEAD OF DELETE ON allInfoPlayer
    FOR EACH ROW
    EXECUTE FUNCTION jogadorTotalInfoDeleteFunction();
	
	