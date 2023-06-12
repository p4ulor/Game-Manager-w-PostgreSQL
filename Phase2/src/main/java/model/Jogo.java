package model;

import jakarta.persistence.*;

@Entity
@Table(name=_TableNames.Jogo)
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 10, unique = true, nullable = false)
    public String id; //"Os jogos têm como identificador uma referência alfanumérica de dimensão 10"

    @Column(name = "url", unique = true, nullable = false)
    public String url;

    @Column(name = "nome", unique = true, nullable = false)
    public String nome;

    public Jogo() {}

    public void setUrl(String url) {
        this.url = url;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
}
