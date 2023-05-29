package model;

import jakarta.persistence.*;

enum Estado {
    Ativo, Inativo, Banido
}

enum Regiao {
    EU, NA, ASIA
}

@Entity
@Table(name="Jogador")
public class Jogador {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Column(name = "username", unique = true, nullable = false)
    String username;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "estado", unique = true, nullable = false)
    Estado estado;

    @Column(name = "regiao", unique = true, nullable = false)
    Regiao regiao;


}
