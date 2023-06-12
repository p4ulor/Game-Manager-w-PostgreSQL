package model;

import jakarta.persistence.*;

@Entity
@Table(name=_TableNames.Cracha)
public class Cracha {

    @EmbeddedId
    public CrachaPK id;

    int pontosAssociados;
    String url;

    public Cracha() {}

    public CrachaPK getId() {
        return id;
    }
    public void setId(CrachaPK id) {
        this.id = id;
    }

    public int getPontosAssociados() {
        return pontosAssociados;
    }
    public void setPontosAssociados(int pontosAssociados) {
        this.pontosAssociados = pontosAssociados;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

}

