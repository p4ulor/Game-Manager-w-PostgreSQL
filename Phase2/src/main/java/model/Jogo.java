package model;

import jakarta.persistence.*;

@Entity
@Table(name="Jogo")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 10, unique = true, nullable = false)
    String id; //"Os jogos têm como identificador uma referência alfanumérica de dimensão 10"

    @Column(name = "text", unique = true, nullable = false)
    String text;

    @Column(name = "nome", unique = true, nullable = false)
    String nome;

    public Jogo() {}
}
