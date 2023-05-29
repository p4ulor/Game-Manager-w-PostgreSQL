package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Crachas_Atribuidos")
public class Crachas_Atribuidos {
    int id_jogador;
    String id_jogo; //max lenght = 10
    String nome;
}
