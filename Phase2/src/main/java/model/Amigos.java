package model;

import jakarta.persistence.*;

@Entity
@Table(name="Amigos")
@IdClass(AmigosID.class)
@NamedQuery(name="Amigos.findAll", query="SELECT a FROM Amigos a")
public class Amigos {
    @Id
    int id_jogador_pedinte;

    @Id
    int id_jogador_destino;

    boolean aceite;

    public Amigos() {}
}

@IdClass(AmigosID.class)
class AmigosID {
    int id_jogador_pedinte;
    int id_jogador_destino;
}