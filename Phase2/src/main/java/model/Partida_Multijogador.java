package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Partida_Multijogador")
public class Partida_Multijogador {
    int id_partida;
    String text; //check 'Por iniciar', 'A aguardar jogadores', 'Em curso', 'Terminada'

}
