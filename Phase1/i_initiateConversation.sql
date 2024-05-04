DO $$ BEGIN END; $$ LANGUAGE PLPGSQL; --for github indexing
-----------------------------------------------------------

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

-- test

call iniciarConversa(0, 'somegroupname'); --paulo creates new conversa
DELETE FROM chat_group_participant WHERE id_chat_group = (SELECT id_chat_group FROM chat_group where nome = 'somegroupname');
DELETE FROM chat_group WHERE nome = 'somegroupname';