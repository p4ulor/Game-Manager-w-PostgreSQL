package model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="Compra")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    Timestamp dataa;
    @Column(name = "preco", columnDefinition = "DECIMAL(6, 2)")
    float preco; //-- Up to 9999.00
    int id_jogador;
    int id_jogo;
}
