DROP FUNCTION IF EXISTS getNumGames_aPlayerPlayed;

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

--test
SELECT getNumGames_aPlayerPlayed(0); --expected 2 for paulo, age of war and minecraft
SELECT getNumGames_aPlayerPlayed(1); --expected 1 for miguel, age of war