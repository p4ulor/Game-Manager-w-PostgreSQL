package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Chat_Group_Participant")
public class Chat_Group_Participant {
    int id_chat_group;
    int id_jogador;
}
