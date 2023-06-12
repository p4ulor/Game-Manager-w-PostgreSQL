package model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name=_TableNames.Pontuacao_Jogador)
public class Pontuacao_Jogador {

    @EmbeddedId
    Pontuacao_JogadorPK id;
    int pontos;

    public Pontuacao_Jogador() {}
}

