DO $$ BEGIN END; $$ LANGUAGE PLPGSQL; --for github indexing

INSERT INTO REGIAO VALUES ('EU');
INSERT INTO REGIAO VALUES ('NA');
INSERT INTO REGIAO VALUES ('ASIA');

INSERT INTO JOGADOR VALUES(0, 'paulo', 'paulo@mail.com', 'Ativo', 'EU');
INSERT INTO JOGADOR VALUES(1, 'miguel', 'miguel@mail.com', 'Ativo', 'EU');
INSERT INTO JOGADOR VALUES(2, 'tyler1', 'tyler1@mail.com', 'Banido', 'NA');
INSERT INTO JOGADOR VALUES(3, 'noobmaster59', 'noobmaster59@mail.com', 'Inativo', 'ASIA');

ALTER SEQUENCE JOGADOR_id_seq RESTART WITH 4;

INSERT INTO AMIGOS VALUES(0, 1, TRUE);
INSERT INTO AMIGOS VALUES(3, 2, FALSE);

--id, nome, id_criador,
INSERT INTO CHAT_GROUP VALUES(0, 'squad', 0);
ALTER SEQUENCE chat_group_id_seq RESTART WITH 1;

--id_chat_group, id_jogador
INSERT INTO CHAT_GROUP_PARTICIPANT VALUES (0, 0);

--id, id_group, dateAndTime, mensagem
INSERT INTO CHATS VALUES(DEFAULT, 0, '2023-05-26 19:10:25-07', 'ay', 0);
INSERT INTO CHATS VALUES(DEFAULT, 0, '2023-05-26 19:10:35-07', 'boas', 0);

--id, nome
INSERT INTO JOGO VALUES('abcefghij0', 'skribll', 'https://skribbl.io/'); --comprado por paulo
INSERT INTO JOGO VALUES('abcefghij1', 'Age Of War', 'https://www.crazygames.com/game/age-of-war'); --comprado por paulo e miguel
INSERT INTO JOGO VALUES('abcefghij2', 'Minecraft', 'https://www.minecraft.net/pt-pt'); --comprado pelo miguel
INSERT INTO JOGO VALUES('abcefghij3', 'Mortal Shell', 'https://store.steampowered.com/app/1110910/Mortal_Shell/'); --nao comprado por nimguem
INSERT INTO JOGO VALUES('abcefghij4', 'Prototype', 'https://store.steampowered.com/app/10150/Prototype/'); --nao comprado por nimguem

--id, dataa, preco, id_jogador, id_jogo
INSERT INTO COMPRA VALUES(DEFAULT, '2023-05-26 00:00:00-00', 1.00, 0, 'abcefghij0'); --paulo compra skribbl
INSERT INTO COMPRA VALUES(DEFAULT, '2023-05-26 00:00:00-00', 9999.00, 0, 'abcefghij1'); --paulo compra Age Of War
INSERT INTO COMPRA VALUES(DEFAULT, '2023-05-26 00:00:00-00', 9999.00, 1, 'abcefghij1'); --miguel compra Age Of War
INSERT INTO COMPRA VALUES(DEFAULT, '2023-05-26 00:00:00-00', 9.00, 1, 'abcefghij2'); --miguel compra Minecraft

--id_jogo, nome, pontosAssociados
INSERT INTO CRACHA VALUES('abcefghij0', 'GOAT', 10000, 'https://knowyourmeme.com/memes/thats-why-hes-the-goat'); --achivement for Minecraft
INSERT INTO CRACHA VALUES ('abcefghij1', 'Sensational', 5000, 'https://youtu.be/3JiUJm3sAv8'); --achivement for Age Of War
INSERT INTO CRACHA VALUES('abcefghij3', 'Mortal Shell achievement', 2000, 'https://knowyourmeme.com/memes/what-was-he-cooking'); --achivement for Mortal Shell

--id_jogador, id_jogo, nome
INSERT INTO CRACHAS_ATRIBUIDOS VALUES(0, 'abcefghij0', 'GOAT');
INSERT INTO CRACHAS_ATRIBUIDOS VALUES(1, 'abcefghij1', 'Sensational');

--id, id_jogo, data_inicio, data_fim, regiao
INSERT INTO PARTIDA VALUES(0, 'abcefghij1', '2023-05-26 10:00:00-00', '2023-05-26 12:00:00-00', 'EU'); --Age Of War, normal, paulo
INSERT INTO PARTIDA VALUES(1, 'abcefghij1', '2023-05-26 13:00:00-00', '2023-05-26 14:00:00-00', 'EU'); --Age Of War, multi, paulo
INSERT INTO PARTIDA VALUES(2, 'abcefghij1', '2023-05-26 15:00:00-00', '2023-05-26 16:00:00-00', 'EU'); --Age Of War, multi, miguel
INSERT INTO PARTIDA VALUES(3, 'abcefghij2', '2023-05-27 15:00:00-00', '2023-05-27 18:00:00-00', 'EU'); --Minecraft, normal, paulo

ALTER SEQUENCE PARTIDA_id_seq RESTART WITH 4;

--id_partida, dificuldade
INSERT INTO PARTIDA_NORMAL VALUES(0, 5); --Age Of War, normal, paulo
INSERT INTO PARTIDA_NORMAL VALUES(3, 5); --Minecraft, normal, paulo

--id_partida, estado
INSERT INTO PARTIDA_MULTIJOGADOR VALUES(1, 'Terminada'); --Age Of War, multi, paulo
INSERT INTO PARTIDA_MULTIJOGADOR VALUES(2, 'Terminada'); --Age Of War, multi, miguel

--id_partida, id_jogador, pontos
INSERT INTO PONTUACAO_JOGADOR VALUES(0, 0, 10000);  --Age Of War, normal, paulo
INSERT INTO PONTUACAO_JOGADOR VALUES(1, 0, 9000); --Age Of War, multi, paulo
INSERT INTO PONTUACAO_JOGADOR VALUES(2, 1, 5000); --Age Of War, multi, miguel
INSERT INTO PONTUACAO_JOGADOR VALUES(3, 0, 1000); --Minecraft, normal, paulo

--id_jogador, numJogosQueComprou, numPartidas, totalPontos
INSERT INTO ESTATISTICA_JOGADOR VALUES(0, 1, 2, 20000); -- estatisticas paulo
INSERT INTO ESTATISTICA_JOGADOR VALUES(1, 1, 1, 5000); -- estatisticas miguel

--id_jogo, totalPartidas, numJogadoresCompraram, totalPontos,
INSERT INTO ESTATISTICA_JOGO VALUES('abcefghij1', 3, 2, 24000); -- estatisticas Age of War
INSERT INTO ESTATISTICA_JOGO VALUES('abcefghij2', 1, 1, 1000); -- estatisticas minecraft

