package model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name=_TableNames.Partida)
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String id_jogo; //max lenght = 10
    Timestamp data_inicio;
    Timestamp data_fim;
    regiao_enum regiao;

    public Partida() {}
}
