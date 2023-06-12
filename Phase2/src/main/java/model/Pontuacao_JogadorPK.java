package model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class Pontuacao_JogadorPK implements Serializable {
    int id_partida;
    int id_jogador;

    public int getId_partida() {
        return id_partida;
    }

    public void setId_partida(int id_partida) {
        this.id_partida = id_partida;
    }

    public int getId_jogador() {
        return id_jogador;
    }

    public void setId_jogador(int id_jogador) {
        this.id_jogador = id_jogador;
    }
}
