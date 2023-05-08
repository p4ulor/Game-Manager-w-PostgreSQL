/*

 */

 CREATE OR REPLACE FUNCTION jogadorTotalInfoDeleteFunction() RETURNS TRIGGER
    LANGUAGE PLPGSQL
    AS
    $$
    BEGIN
        IF tg_op <> 'DELETE' THEN
            RAISE EXCEPTION 'Invalid Trigger';
        END IF;

    UPDATE JOGADOR SET estado = 'Banido' WHERE JOGADOR.id = old.id;
    RETURN NULL;
    END;
    $$;

CREATE OR REPLACE TRIGGER jogadorTotalInfoDeleteTrigger
    INSTEAD OF DELETE ON allInfoPlayer
    FOR EACH ROW
    EXECUTE FUNCTION jogadorTotalInfoDeleteFunction();


-- test
DELETE FROM allInfoPlayer Where username = 'miguel';

