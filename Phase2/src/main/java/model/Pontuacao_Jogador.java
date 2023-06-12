package model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name=_TableNames.Pontuacao_Jogador)
public class Pontuacao_Jogador {

    @EmbeddedId
    public Pontuacao_JogadorPK id;
    public int pontos;

    public Pontuacao_Jogador() {}

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
}

