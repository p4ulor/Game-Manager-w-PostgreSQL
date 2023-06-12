package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name=_TableNames.Estatistica_Jogador)
public class Estatistica_Jogador {
    @Id
    public int id_jogador;
    public int numJogosQueComprou;
    public int numPartidas;
    public int totalPontos;

    public Estatistica_Jogador() {}

    public void setNumJogosQueComprou(int numJogosQueComprou) {
        this.numJogosQueComprou = numJogosQueComprou;
    }
    public void setNumPartidas(int numPartidas) {
        this.numPartidas = numPartidas;
    }
    public void setTotalPontos(int totalPontos) {
        this.totalPontos = totalPontos;
    }
}
