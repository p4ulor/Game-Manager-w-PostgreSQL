DO $$ BEGIN END; $$ LANGUAGE PLPGSQL; --for github indexing
-----------------------------------------------------------
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

        SELECT totalPontos INTO points FROM gamePointsPerPlayer(idJogo) WHERE gamePointsPerPlayer.idjogador = associarCrachaLogica.idJogador;
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

-- abcefghij1
-- Sensational

    call associarCrachaTansaction(0, 'abcefghij1', 'Sensational'); --ads badge 'Sensational' to game Age Of War for paulo
DELETE FROM crachas_atribuidos where id_jogador = 0 AND id_jogo = 'abcefghij1' AND nome = 'Sensational';