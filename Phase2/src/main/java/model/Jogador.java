package model;

import jakarta.persistence.*;
import utils.Utils;

@Entity
@Table(name="Jogador")
public class Jogador extends Utils {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jogador_id_generator")
    @SequenceGenerator(name = "jogador_id_generator", sequenceName = "jogador_id_seq", allocationSize = 1) //sequenceName is the same as the sequence name in the postgres DB. allocationSize -> https://stackoverflow.com/a/12745902
    int id;

    @Column(name = "username", unique = true, nullable = false)
    String username;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "estado", nullable = false, columnDefinition = "estado_enum")
    @Enumerated(EnumType.STRING)
    estado_enum estado = estado_enum.Ativo;

    @Column(name = "regiao", nullable = false)
    @Enumerated(EnumType.STRING)
    regiao_enum regiao;

    public Jogador(){}

    public Jogador(String username, String email, regiao_enum regiao) {
        this.username = username;
        this.email = email;
        this.regiao = regiao;
    }

    public void setEstado(estado_enum estado) {
        this.estado = estado;
    }
}
