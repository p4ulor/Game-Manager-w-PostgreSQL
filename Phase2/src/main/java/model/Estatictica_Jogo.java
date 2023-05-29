package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Estatictica_Jogo")
public class Estatictica_Jogo {
    String id_jogo; //lenght = 10
    int totalPartidas;
    int numJogadoreQueCompraram;
    int totalPontos;
}
