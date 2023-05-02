-- Stored procedures:

-- "criar o jogador, dados os seus email, região e username":
--using this instead of DEFAULT (in the id) to avoid problems, because inserting with an explicit ID doesn't increase the
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

--test
CALL criarJogador('new', 'new@mail.com', 'NA');

--desativar ou banir o jogador, dados os seus email, região e username:
CREATE OR REPLACE PROCEDURE mudarEstadoJogador(usernamee TEXT, novoEstado estado_enum) language plpgsql as $$
    begin
        UPDATE JOGADOR
        SET estado = novoEstado
        WHERE username = usernamee;
    end;
$$;

--test
CALL mudarEstadoJogador('tyler1', 'Ativo');
