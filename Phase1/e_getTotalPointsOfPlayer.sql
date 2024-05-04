DO $$ BEGIN END; $$ LANGUAGE PLPGSQL; --for github indexing
DROP FUNCTION IF EXISTS getTotalPointsOfPlayer;

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

--test, expected = 20000, 
-- Age Of War, normal, paulo, 10000
-- Age Of War, multi, paulo, 9000
-- Minecraft, normal, paulo, 1000
SELECT getTotalPointsOfPlayer(0);