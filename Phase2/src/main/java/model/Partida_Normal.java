package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Partida_Normal")
public class Partida_Normal {
    int id_partida;
    int dificuldade; // 1-5
    
}
