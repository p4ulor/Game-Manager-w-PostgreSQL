package model;

import jakarta.persistence.*;

@Entity
@Table(name=_TableNames.Chat_Group)
public class Chat_Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id; //(id_chat_group)

    @Column(name = "nome", nullable = false)
    String nome;

    @Column(name = "id_criador", nullable = false)
    int id_criador;

    public Chat_Group() {}

    static String a = "";
}

