DO $$ BEGIN END; $$ LANGUAGE PLPGSQL; --for github indexing
-----------------------------------------------------------
/*
    Obtains
    id, estado, email, username, número de diferentes jogos que ja participou, número de partidas feitas e total de pontos,
    Dos jogadores cujo estado seja diferente de “Banido”. 
    Deve implementar na vista os cálculos sem aceder às tabelas de estatísticas.
*/

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
	
	WHERE estado !='Banido'
	
--test
SELECT * FROM allInfoPlayer;