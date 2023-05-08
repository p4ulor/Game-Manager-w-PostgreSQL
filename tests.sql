/*
 TESTS SCRIPT
 */

do $testes$
BEGIN


 -- Testing d)


 -- Testing e)


 -- Testing f)


 -- Testing g)
 DO
 $$
  DECLARE
   code char(5) DEFAULT '00000';
   msg  text;
  BEGIN
   BEGIN
        -- expected [0, 19000] and [1, 5000]
    ASSERT EXISTS (SELECT * FROM PontosJogoPorjogador('abcefghij1') WHERE idjogador = 1 AND totalpontos = 5000);
    ASSERT EXISTS (SELECT * FROM PontosJogoPorjogador('abcefghij1') WHERE idjogador = 0 AND totalpontos = 19000);
   EXCEPTION
    WHEN OTHERS THEN
     GET STACKED DIAGNOSTICS
      code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
     RAISE NOTICE 'code=%, msg=%',code,msg;
   END;
  END;
 $$;


 -- Testing h)

 DO
 $$
  DECLARE
   code char(5) DEFAULT '00000';
   msg  text;
  BEGIN
   BEGIN

    ASSERT NOT EXISTS (SELECT * FROM crachas_atribuidos
                                WHERE id_jogador = 0 AND id_jogo = 'abcefghij1' AND nome = 'Sensational');

    CALL associarCrachaLogica(0, 'abcefghij1', 'Sensational');

    ASSERT EXISTS (SELECT * FROM crachas_atribuidos
                   WHERE id_jogador = 0 AND id_jogo = 'abcefghij1' AND nome = 'Sensational');

    DELETE FROM crachas_atribuidos WHERE id_jogador = 0 AND id_jogo = 'abcefghij1' AND nome = 'Sensational';

   EXCEPTION
    WHEN OTHERS THEN
     GET STACKED DIAGNOSTICS
      code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
     RAISE NOTICE 'code=%, msg=%',code,msg;
   END;
  END;
 $$;


 -- Testing i)

 DO
 $$
  DECLARE
   code char(5) DEFAULT '00000';
   msg  text;
  BEGIN
   BEGIN

    ASSERT NOT EXISTS (SELECT * FROM chat_group_participant
                       WHERE id_chat_group = (SELECT id_chat_group FROM chat_group WHERE nome = 'somegroupname'));

    ASSERT NOT EXISTS(SELECT * FROM chat_group WHERE nome = 'somegroupname');

    CALL iniciarConversaLogica(0, 'somegroupname');

    ASSERT EXISTS (SELECT *  FROM chat_group_participant
                   WHERE id_chat_group = (SELECT id_chat_group FROM chat_group WHERE nome = 'somegroupname'));

    ASSERT EXISTS(SELECT * FROM chat_group WHERE nome = 'somegroupname');

    DELETE  FROM chat_group_participant
    WHERE id_chat_group = (SELECT id_chat_group FROM chat_group WHERE nome = 'somegroupname');

    DELETE FROM chat_group WHERE nome = 'somegroupname';

   EXCEPTION
    WHEN OTHERS THEN
     GET STACKED DIAGNOSTICS
      code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
     RAISE NOTICE 'code=%, msg=%',code,msg;
   END;
  END;
 $$;

 -- Testing j)


 -- Testing k)

 DO
 $$
  DECLARE
   code char(5) DEFAULT '00000';
   msg  text;
  BEGIN
   BEGIN

    ASSERT NOT EXISTS (SELECT * FROM CHATS WHERE id_group = 0 AND mensagem = 'somemessage');
    CALL enviarMensagemLogica(0, 0, 'somemessage');
    ASSERT EXISTS (SELECT * FROM CHATS WHERE id_group = 0 AND mensagem = 'somemessage');
    DELETE FROM CHATS WHERE id_group = 0 AND mensagem = 'somemessage';

   EXCEPTION
    WHEN OTHERS THEN
     GET STACKED DIAGNOSTICS
      code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
     RAISE NOTICE 'code=%, msg=%',code,msg;
   END;
  END;
 $$;

 -- Testing l)


 -- Testing m)


 -- Testing n)

 DO
 $$
  DECLARE
   code char(5) DEFAULT '00000';
   msg  text;
   state_holder JOGADOR.estado%TYPE;
  BEGIN
      SELECT estado INTO state_holder FROM JOGADOR WHERE id = 1;
   BEGIN

       ASSERT EXISTS (SELECT * FROM allInfoPlayer where username = 'miguel');
       DELETE FROM allInfoPlayer Where username = 'miguel';
       ASSERT NOT EXISTS (SELECT * FROM allInfoPlayer where username = 'miguel');

       UPDATE JOGADOR SET estado = state_holder WHERE id = 1;

   EXCEPTION
    WHEN OTHERS THEN
     GET STACKED DIAGNOSTICS
      code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
     RAISE NOTICE 'code=%, msg=%',code,msg;
   END;
  END;
 $$;


END; $testes$