package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Estatistica_Jogador")
public class Estatistica_Jogador {
    int id_jogador;
    int numJogosQueComprou;
    int numPartidas;
    int totalPontos;

}
