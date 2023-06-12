package model;

import jakarta.persistence.*;

@Entity
@Table(name=_TableNames.Chat_Group_Participant)
public class Chat_Group_Participant {
    @EmbeddedId
    public Chat_Group_ParticipantPK id;

    public Chat_Group_Participant() {}
}
