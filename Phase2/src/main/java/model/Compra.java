package model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name=_TableNames.Compra)
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    public Timestamp dataa;

    @Column(name = "preco", columnDefinition = "DECIMAL(6, 2)")
    public float preco; //-- Up to 9999.00
    public int id_jogador;
    public int id_jogo;

    public Compra() {}

    public void setDataa(Timestamp dataa) {
        this.dataa = dataa;
    }
    public void setPreco(float preco) {
        this.preco = preco;
    }
    public void setId_jogador(int id_jogador) {
        this.id_jogador = id_jogador;
    }
    public void setId_jogo(int id_jogo) {
        this.id_jogo = id_jogo;
    }
}
