DO $$ BEGIN END; $$ LANGUAGE PLPGSQL; --for github indexing
-----------------------------------------------------------
/*
    Criar os mecanismos necessários para que, de forma automática, quando uma partida
    termina, se proceda à atribuição de crachás do jogo a que ela pertence. 
*/

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

-- gets players involved in the partida
-- calculates what badges to give according to the points

--test
INSERT INTO PARTIDA VALUES(4, 'abcefghij3', NOW(), null, 'EU'); --Mortal Shell, normal, paulo
INSERT INTO PONTUACAO_JOGADOR VALUES(4, 0, 2000); --Mortal Shell, normal, paulo
--I should be able to obtain 'Sensational' badge
UPDATE PARTIDA SET data_fim = NOW() WHERE id = 4;
SELECT * FROM CRACHAS_ATRIBUIDOS WHERE id_jogador = 0;
