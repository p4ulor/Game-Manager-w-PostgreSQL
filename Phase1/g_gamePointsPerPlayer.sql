DO $$ BEGIN END; $$ LANGUAGE PLPGSQL; --for github indexing
-----------------------------------------------------------
/*
    Its a simple query so the rows are all returned at the same time using 'RETURN QUERY' instead of 'RETURN NEXT'.
    A 'SUM' is used in the values received to sum all the points of the same player when the GROUP BY id_jogador is done.
    - Will probably be read-committed
    RETURN - In case of game not having any players with points, will return empty query.
*/

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

--test
SELECT * FROM gamePointsPerPlayer('abcefghij1'); -- expected [0, 19000] and [1, 5000]. abcefghij1 = Age Of War

