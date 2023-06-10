package daos;

import model.estado_enum;

public class PlayerTotalInfo {
    int id;
    estado_enum estado;
    String email;
    String username;
    int numgamesplayed;
    int totalpoints;
    int numtotalpartidas;

    public PlayerTotalInfo(int id, estado_enum estado, String email, String username, int numgamesplayed, int totalpoints, int numtotalpartidas) {
        this.id = id;
        this.estado = estado;
        this.email = email;
        this.username = username;
        this.numgamesplayed = numgamesplayed;
        this.totalpoints = totalpoints;
        this.numtotalpartidas = numtotalpartidas;
    }

    @Override
    public String toString() {
        return "PlayerTotalInfo{" +
                "id=" + id +
                ", estado=" + estado +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", numgamesplayed=" + numgamesplayed +
                ", totalpoints=" + totalpoints +
                ", numtotalpartidas=" + numtotalpartidas +
                '}';
    }
}
