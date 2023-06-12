package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name=_TableNames.Estatictica_Jogo)
public class Estatistica_Jogo {
    @Id
    public String id_jogo; //lenght = 10
    public int totalPartidas;
    public int numJogadoreQueCompraram;
    public int totalPontos;

    public Estatistica_Jogo() {}

    public void setTotalPartidas(int totalPartidas) {
        this.totalPartidas = totalPartidas;
    }
    public void setNumJogadoreQueCompraram(int numJogadoreQueCompraram) {
        this.numJogadoreQueCompraram = numJogadoreQueCompraram;
    }
    public void setTotalPontos(int totalPontos) {
        this.totalPontos = totalPontos;
    }
}
