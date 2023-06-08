package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name="Pontuacao_Jogador")
@IdClass(Pontuacao_JogadorID.class)
public class Pontuacao_Jogador {
    @Id
    int id_partida;
    @Id
    int id_jogador;
    int pontos;

    public Pontuacao_Jogador() {}
}

@IdClass(Pontuacao_JogadorID.class)
class Pontuacao_JogadorID {
    int id_partida;
    int id_jogador;
}