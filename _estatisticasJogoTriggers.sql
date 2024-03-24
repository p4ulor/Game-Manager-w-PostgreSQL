DO $$ BEGIN END; $$ LANGUAGE PLPGSQL; --for github indexing
--Compras

CREATE OR REPLACE FUNCTION _autoUpdateNumJogadoresComparam()  RETURNS TRIGGER LANGUAGE PLPGSQL AS $$
    DECLARE

    BEGIN
        RAISE NOTICE 'Triggered _autoUpdateNumJogadoresComparam';

        IF NOT EXISTS (SELECT * FROM ESTATISTICA_JOGO WHERE id_jogo = NEW.id_jogo) then
            INSERT INTO ESTATISTICA_JOGO VALUES(NEW.id_jogo, 0, 0, 0);
        END IF;

        UPDATE ESTATISTICA_JOGO 
        SET numJogadoresCompraram = numJogadoresCompraram + 1 
        WHERE NEW.id_jogo = ESTATISTICA_JOGO.id_jogo;

		RETURN NULL;
    END;
$$;


CREATE OR REPLACE TRIGGER autoUpdateNumJogadoresComparam AFTER INSERT ON COMPRA
    FOR EACH ROW
    EXECUTE FUNCTION _autoUpdateNumJogadoresComparam()
; 

--test
INSERT INTO COMPRA VALUES(DEFAULT, '2023-05-26 00:00:00-00', 9999.00, 0, 'abcefghij3'); --paulo compra Mortal Shell
SELECT * FROM ESTATISTICA_JOGO WHERE id_jogo = 'abcefghij3';

----------------------------------------------------------------------------------------------------
--Partidas

CREATE OR REPLACE FUNCTION _autoUpdatePartidas()  RETURNS TRIGGER LANGUAGE PLPGSQL AS $$
    DECLARE

    BEGIN
        RAISE NOTICE 'Triggered _autoUpdatePartidas';

        IF NOT EXISTS (SELECT * FROM ESTATISTICA_JOGO WHERE id_jogo = NEW.id_jogo) then
            INSERT INTO ESTATISTICA_JOGO VALUES(NEW.id_jogo, 0, 0, 0);
        END IF;

        UPDATE ESTATISTICA_JOGO 
        SET totalPartidas = totalPartidas + 1 
        WHERE NEW.id_jogo = ESTATISTICA_JOGO.id_jogo;

		RETURN NULL;
    END;
$$;


CREATE OR REPLACE TRIGGER autoUpdatePartidas AFTER INSERT ON PARTIDA
    FOR EACH ROW
    EXECUTE FUNCTION _autoUpdatePartidas()
;

--test
INSERT INTO PARTIDA VALUES(DEFAULT, 'abcefghij2', '2023-05-28 15:00:00-00', '2023-05-28 18:00:00-00', 'EU'); --Minecraft, normal, paulo
SELECT * FROM ESTATISTICA_JOGO WHERE id_jogo = 'abcefghij2';

----------------------------------------------------------------------------------------------------
--Pontos

CREATE OR REPLACE FUNCTION _autoUpdatePontos()  RETURNS TRIGGER LANGUAGE PLPGSQL AS $$
    DECLARE
        idJogo CHAR(10);
    BEGIN
        RAISE NOTICE 'Triggered _autoUpdatePontos';

        SELECT id_jogo
        FROM PARTIDA
        WHERE NEW.id_partida = PARTIDA.id
        INTO idJogo;

        IF NOT EXISTS (SELECT * FROM ESTATISTICA_JOGO WHERE id_jogo = idJogo) then
            INSERT INTO ESTATISTICA_JOGO VALUES(idJogo, 0, 0, 0);
        END IF;

        UPDATE ESTATISTICA_JOGO 
        SET totalPontos = totalPontos + NEW.pontos 
        WHERE ESTATISTICA_JOGO.id_jogo = idJogo;

		RETURN NULL;
    END;
$$;


CREATE OR REPLACE TRIGGER autoUpdatePontos AFTER INSERT ON PONTUACAO_JOGADOR
    FOR EACH ROW
    EXECUTE FUNCTION _autoUpdatePontos()
;

--test
INSERT INTO PARTIDA VALUES(DEFAULT, 'abcefghij2', '2023-05-28 15:00:00-00', '2023-05-28 18:00:00-00', 'EU'); --Minecraft, normal, paulo

DO $$
DECLARE v_studentname text;
	val INT;
BEGIN
	SELECT LAST_VALUE FROM PARTIDA_id_seq INTO val;
	INSERT INTO PONTUACAO_JOGADOR VALUES(val, 0, 50000);
	
END $$;
SELECT * FROM ESTATISTICA_JOGO WHERE id_jogo = 'abcefghij2';