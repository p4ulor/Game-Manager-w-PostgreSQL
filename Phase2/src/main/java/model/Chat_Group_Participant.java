package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name="Chat_Group_Participant")
@IdClass(Chat_Group_Participant.class) //https://www.baeldung.com/jpa-composite-primary-keys https://stackoverflow.com/a/212371
public class Chat_Group_Participant {
    @Id
    int id_chat_group;
    @Id
    int id_jogador;

    public Chat_Group_Participant() {}
}
