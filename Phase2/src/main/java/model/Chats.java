package model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name="Chats")
public class Chats {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    int id_group;
    Date dateAndTime;
    String mensagem;
    int id_jogador;
}