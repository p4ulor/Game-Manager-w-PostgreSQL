package model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;


@Embeddable
public class CrachaPK implements Serializable {
    String id_jogo;
    String nome;

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
