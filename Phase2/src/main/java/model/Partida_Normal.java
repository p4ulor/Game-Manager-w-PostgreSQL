package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name=_TableNames.Partida_Normal)
public class Partida_Normal {
    @Id
    int id_partida;
    int dificuldade; // 1-5

    public Partida_Normal() {}
}
