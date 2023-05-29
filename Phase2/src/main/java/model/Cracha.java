package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Cracha")
public class Cracha {
    String id_jogo; //max lenght = 10
    String text;
    int pontosAssociados;
    String url;
}
