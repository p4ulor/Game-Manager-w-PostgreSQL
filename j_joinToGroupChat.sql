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

--test
call joinToGroupChat(1, 0); --miguel joins to conversa
DELETE FROM chat_group WHERE id_jogador = 1;