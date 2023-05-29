package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Amigos")
public class Amigos {

    @Column(name = "id_jogador_pedinte", nullable = false)
    int id_jogador_pedinte;

    int id_jogador_destino;
    boolean aceite;
}
