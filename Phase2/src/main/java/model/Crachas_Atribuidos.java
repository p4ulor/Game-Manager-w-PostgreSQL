package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name="Crachas_Atribuidos")
@IdClass(Crachas_Atribuidos.class)
public class Crachas_Atribuidos {
    @Id
    int id_jogador;
    @Id
    String id_jogo; //max lenght = 10
    @Id
    String nome;

    public Crachas_Atribuidos() {}
}
