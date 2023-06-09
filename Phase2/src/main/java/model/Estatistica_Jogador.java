package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name=_TableNames.Estatistica_Jogador)
public class Estatistica_Jogador {
    @Id
    int id_jogador;
    int numJogosQueComprou;
    int numPartidas;
    int totalPontos;

    public Estatistica_Jogador() {}
}
