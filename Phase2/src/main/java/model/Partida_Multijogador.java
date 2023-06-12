package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name=_TableNames.Partida_Multijogador)
public class Partida_Multijogador {
    public static String[] estadosPossiveis = {"Por iniciar", "A aguardar jogadores", "Em curso", "Terminada" }; //para ficar igual como temos no create tables
    @Id
    public int id_partida;
    public String estado;

    public Partida_Multijogador() {}

    public void setEstado(String estado) {
        boolean exists = false;
        for (String element : estadosPossiveis) {
            if (element.equals(estado)) {
                exists = true;
            }
        }
        if(! exists) throw new IllegalArgumentException("Invalid estado");
        this.estado = estado;
    }
}
