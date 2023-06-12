package model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class Chat_Group_ParticipantPK implements Serializable {
    int id_chat_group;
    int id_jogador;
}
