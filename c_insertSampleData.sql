INSERT INTO REGIAO VALUES ('EU');
INSERT INTO REGIAO VALUES ('NA');
INSERT INTO REGIAO VALUES ('ASIA');

ALTER SEQUENCE JOGADOR_id_seq RESTART WITH 1;

INSERT INTO JOGADOR VALUES(0, 'paulo', 'paulo@mail.com', 'Ativo', 'EU');
INSERT INTO JOGADOR VALUES(1, 'miguel', 'miguel@mail.com', 'Ativo', 'EU');
INSERT INTO JOGADOR VALUES(2, 'tyler1', 'tyler1@mail.com', 'Banido', 'NA');
INSERT INTO JOGADOR VALUES(3, 'noobmaster59', 'noobmaster59@mail.com', 'Inativo', 'ASIA');

INSERT INTO AMIGOS VALUES(0, 1, TRUE);
INSERT INTO AMIGOS VALUES(3, 2, FALSE);

--id, nome, id_criador, participantes
INSERT INTO CHAT_GROUP VALUES(0, 'squad', 0, '{0, 1}');

--id, id_group, dateAndTime, mensagem
INSERT INTO CHATS VALUES(DEFAULT, 0, '2023-05-26 19:10:25-07', 'ay');
INSERT INTO CHATS VALUES(DEFAULT, 0, '2023-05-26 19:10:35-07', 'boas');

INSERT INTO JOGO VALUES(0, 'skribll', 'https://skribbl.io/', 0, 0, 0);
INSERT INTO JOGO VALUES(1, 'Age Of War', 'https://www.crazygames.com/game/age-of-war', 0, 0, 0);
INSERT INTO JOGO VALUES(2, 'Minecraft', 'https://www.minecraft.net/pt-pt', 0, 0, 0);

--id, dataa, preco, id_jogador, id_jogo
INSERT INTO COMPRA VALUES(DEFAULT, '2023-05-26 00:00:00-00', 9999.00, 0, 1); --compra paulo
INSERT INTO COMPRA VALUES(DEFAULT, '2023-05-26 00:00:00-00', 9999.00, 1, 1); --compra miguel
INSERT INTO COMPRA VALUES(DEFAULT, '2023-05-26 00:00:00-00', 9.00, 1, 2); --compra miguel

--id_jogo, nome, pontosAssociados
INSERT INTO CRACHA VALUES(0, 'GOAT', 10000, 'https://knowyourmeme.com/memes/thats-why-hes-the-goat');
INSERT INTO CRACHA VALUES (1, 'Sensational', 5000, 'https://youtu.be/3JiUJm3sAv8');

--id_jogador, id_jogo, nome
INSERT INTO CRACHAS_ATRIBUIDOS VALUES(0, 0, 'GOAT');
INSERT INTO CRACHAS_ATRIBUIDOS VALUES(1, 1, 'Sensational');

--id, id_jogo, data_inicio, data_fim, regiao
INSERT INTO PARTIDA VALUES(0, 1, '2023-05-26 10:00:00-00', '2023-05-26 12:00:00-00', 'EU');
INSERT INTO PARTIDA VALUES(1, 1, '2023-05-26 13:00:00-00', '2023-05-26 14:00:00-00', 'EU');
INSERT INTO PARTIDA VALUES(2, 1, '2023-05-26 15:00:00-00', '2023-05-26 16:00:00-00', 'EU');

--id_partida, dificuldade
INSERT INTO PARTIDA_NORMAL VALUES(0, 5); --paulo

--id_partida, estado
INSERT INTO PARTIDA_MULTIJOGADOR VALUES(1, 'Terminada'); --paulo
INSERT INTO PARTIDA_MULTIJOGADOR VALUES(2, 'Terminada'); --miguel

--id_partida, id_jogador, pontos
INSERT INTO PONTUACAO_JOGADOR VALUES(0, 0, 10000); --paulo
INSERT INTO PONTUACAO_JOGADOR VALUES(1, 0, 9000); --paulo
INSERT INTO PONTUACAO_JOGADOR VALUES(2, 1, 5000); --miguel

--id_jogador, numJogosQueComprou, numPartidas, totalPontos
INSERT INTO ESTATISTICA VALUES(0, 1, 2, 19000);
INSERT INTO ESTATISTICA VALUES(1, 1, 1, 5000);
