package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Pontuacao_Jogador")
public class Pontuacao_Jogador {

    int id_partida;
    int id_jogador;
    int pontos;
}
