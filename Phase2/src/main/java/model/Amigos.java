package model;

import jakarta.persistence.*;

@Entity
@Table(name=_TableNames.Amigos)
@NamedQuery(name="Amigos.findAll", query="SELECT a FROM "+_TableNames.Amigos+" a")
public class Amigos {
    @EmbeddedId
    AmigosPK id;
    boolean aceite;

    public Amigos() {}
}
