package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name=_TableNames.Partida_Multijogador)
public class Partida_Multijogador {
    @Id
    int id_partida;
    String text; //check 'Por iniciar', 'A aguardar jogadores', 'Em curso', 'Terminada'

    public Partida_Multijogador() {}
}
