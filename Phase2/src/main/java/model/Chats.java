package model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name=_TableNames.Chats)
public class Chats {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    public int id_group;
    public Date dateAndTime;
    public String mensagem;
    public int id_jogador;

    public Chats() {}

    public void setId_group(int id_group) {
        this.id_group = id_group;
    }
    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    public void setId_jogador(int id_jogador) {
        this.id_jogador = id_jogador;
    }
}
