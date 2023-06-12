package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name=_TableNames.Partida_Normal)
public class Partida_Normal {
    @Id
    public int id_partida;
    public int dificuldade; // 1-5

    public Partida_Normal() {}

    public void setDificuldade(int dificuldade) {
        this.dificuldade = dificuldade;
    }
}
