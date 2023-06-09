package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name=_TableNames.Estatictica_Jogo)
public class Estatictica_Jogo {
    @Id
    String id_jogo; //lenght = 10
    int totalPartidas;
    int numJogadoreQueCompraram;
    int totalPontos;

    public Estatictica_Jogo() {}
}
