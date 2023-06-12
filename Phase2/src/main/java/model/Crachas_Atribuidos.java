package model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name=_TableNames.Crachas_Atribuidos)
public class Crachas_Atribuidos {
    @EmbeddedId
    Crachas_AtribuidosPK id;

    public Crachas_AtribuidosPK getId() {
        return id;
    }

    public void setId(Crachas_AtribuidosPK id) {
        this.id = id;
    }

    public Crachas_Atribuidos() {}
}
