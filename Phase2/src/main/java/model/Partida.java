package model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name=_TableNames.Partida)
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    public String id_jogo; //max lenght = 10
    public Timestamp data_inicio;
    public Timestamp data_fim;
    public regiao_enum regiao;

    public Partida() {}

}
