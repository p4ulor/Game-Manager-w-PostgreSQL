CREATE OR REPLACE PROCEDURE enviarMensagem(idJogador JOGADOR.id%TYPE, idConversa CHAT_GROUP.id%TYPE, mensagem CHATS.mensagem%TYPE)
    LANGUAGE PLPGSQL
    AS
    $$
    BEGIN
        COMMIT;
        SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
        CALL enviarMensagemTransacao(idJogador, idConversa, mensagem);
    END;
    $$;

CREATE OR REPLACE PROCEDURE enviarMensagemTransacao(idJogador JOGADOR.id%TYPE, idConversa CHAT_GROUP.id%TYPE, mensagem CHATS.mensagem%TYPE)
    LANGUAGE PLPGSQL
    AS
    $$
    DECLARE
        code char(5) DEFAULT '00000';
        msg  text;
    BEGIN
        CALL enviarMensagemLogica(idJogador, idConversa, mensagem);
        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Ocorreu algum erro';
                GET STACKED DIAGNOSTICS
                    code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
                RAISE NOTICE 'code=%, msg=%',code,msg;
                ROLLBACK;
    END;
    $$;

CREATE OR REPLACE PROCEDURE enviarMensagemLogica(idJogador JOGADOR.id%TYPE, idConversa CHAT_GROUP.id%TYPE, mensagem CHATS.mensagem%TYPE)
    LANGUAGE PLPGSQL
    AS
    $$
    BEGIN
        IF NOT EXISTS (SELECT * FROM jogador where jogador.id = idJogador) then
            RAISE EXCEPTION 'Player does not exist %', idJogador;
        END IF;

        IF NOT EXISTS (SELECT * FROM CHAT_GROUP where CHAT_GROUP.id = idConversa) then
            RAISE EXCEPTION 'Chat group does not exist %', idConversa;
        END IF;

        IF NOT EXISTS (SELECT * FROM CHAT_GROUP_PARTICIPANT where CHAT_GROUP_PARTICIPANT.id_chat_group = idConversa AND CHAT_GROUP_PARTICIPANT.id_jogador = idJogador) then
            RAISE EXCEPTION 'Player % is not participant of chosen chat group %', idJogador, idConversa;
        END IF;

        INSERT INTO CHATS VALUES (DEFAULT, enviarMensagemLogica.idConversa, DEFAULT, enviarMensagemLogica.mensagem, enviarMensagemLogica.idJogador);

    END;
    $$;

--test
call enviarMensagem(0, 0, 'somemessage');
DELETE FROM CHATS WHERE id_group = 0 AND mensagem = 'somemessage';