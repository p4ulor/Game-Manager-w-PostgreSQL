package model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class AmigosPK implements Serializable { //must implement Serializable for the key to work
    int id_jogador_pedinte;
    int id_jogador_destino;

    public int getId_jogador_pedinte() { return id_jogador_pedinte; }
    public void setId_jogador_pedinte(int id_jogador_pedinte) { this.id_jogador_pedinte = id_jogador_pedinte; }

    public int getId_jogador_destino() { return id_jogador_destino; }
    public void setId_jogador_destino(int id_jogador_destino) { this.id_jogador_destino = id_jogador_destino; }
}
