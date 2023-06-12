package model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class Crachas_AtribuidosPK implements Serializable { //must implement Serializable in order to work

    int id_jogador;
    String id_jogo; //max lenght = 10
    String nome;

    public int getId_jogador() {
        return id_jogador;
    }
    public void setId_jogador(int id_jogador) {
        this.id_jogador = id_jogador;
    }

    public String getId_jogo() {
        return id_jogo;
    }
    public void setId_jogo(String id_jogo) {
        this.id_jogo = id_jogo;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
}
