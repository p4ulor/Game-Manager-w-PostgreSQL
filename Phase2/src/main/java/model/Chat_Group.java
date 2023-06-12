package model;

import jakarta.persistence.*;

@Entity
@Table(name=_TableNames.Chat_Group)
public class Chat_Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id; //(id_chat_group)

    @Column(name = "nome", nullable = false)
    public String nome;

    @Column(name = "id_criador", nullable = false)
    public int id_criador;

    public Chat_Group() {}

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setId_criador(int id_criador) {
        this.id_criador = id_criador;
    }
}

