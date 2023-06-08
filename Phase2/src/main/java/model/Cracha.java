package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name="Cracha")
@IdClass(CrachaID.class)
public class Cracha {
    @Id
    String id_jogo; //max lenght = 10
    @Id
    String nome;
    int pontosAssociados;
    String url;

    public Cracha() {}
}

@IdClass(CrachaID.class)
class CrachaID {
    String id_jogo;
    String nome;
}